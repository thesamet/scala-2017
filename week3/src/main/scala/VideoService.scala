import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import VideoService._
import VideoActor._
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout

import scala.concurrent.duration._
import scala.collection.mutable
import scala.concurrent.{Await, Future}
import scala.io.StdIn

// Here we implement an actor system that holds the state of VI + Trends.
// Let's start by implementing an actor that represents the state of a single video.
// It can receive the following messages:
object VideoActor {
  // *** Requests ***
  sealed trait VideoActorRequest

  // Updates the description of the video to the provided description
  case class UpdateDescriptionRequest(description: String) extends VideoActorRequest

  // Associate the video with the given creator id.
  case class AssociateCreatorRequest(creatorId: String) extends VideoActorRequest

  // Disassociate the video with the given creator id.
  // This should disassociate the video from the creator
  // ONLY if it is currently associated with it (See DMV-708 for
  // inspiration :)
  case class DisassociateCreatorRequest(creatorId: String) extends VideoActorRequest

  // Appends the given value to the trends vector. This represents a data
  // point that we crawled (like the number of views)
  case class AddTrendPointRequest(value: Int) extends VideoActorRequest

  // Requests the actor to return its current state back to the caller
  case object GetDocumentRequest extends VideoActorRequest

  // *** Responses ***

  // Returned in response to GetDocument()
  case class ReplyDocument(description: String, creatorId: Option[String], trends: Vector[Int])
}

class VideoActor extends Actor with ActorLogging {
  var description: String = ""
  var creatorId: Option[String] = None
  // Note that the vector is immutable, but we have a 'var' reference to it, so upon update
  // we will replace the entire vector.
  var trends: Vector[Int] = Vector.empty

  // You need to implement the following cases by mutating the state:
  override def receive = {
    case UpdateDescriptionRequest(newDescription) =>
      log.info("Updating my description to '{}'", newDescription)

    case AssociateCreatorRequest(newCreatorId) => creatorId = Some(newCreatorId)

    case DisassociateCreatorRequest(oldCreatorId) =>
      // Set creatorId to None only it is different from the given oldCreatorId.
      // Careful: creatorId is an Option[String].

    case AddTrendPointRequest(value) =>
      // Hint: "vector :+ value" performs a functional "append", it returns a new vector with the value
      // appended to it.

    case GetDocumentRequest =>
      sender() ! ReplyDocument(description, creatorId, trends)
  }
}

// Okay! It is time to implement the main service actor which will forward messages
// to the individual video actors.
object VideoService {
  // The following messages are supported by the video service

  // Sent when we discover a new video. The service should launch a new actor.
  case class RegisterNewVideoRequest(videoId: String)

  // This request asks the service to forward the given request to the video actor.
  // We will use "forward" so the reply of the video actor will go directly back to
  // the sender of this message (and not to the service itself)
  case class ForwardVideoRequest(videoId: String, request: VideoActorRequest)

  // Aggregate the trends from all the actors. In this exercise, we're just going to
  // concatenate all the trend vectors.
  case object AggregateTrendsRequest

  // Responses
  case class AggregatedTrendsReply(trends: Vector[Int])
}

class VideoService extends Actor {
  // This maps between vector id and the video actors.  Note the encapsulation:
  // we never get a reference to a VideoActor class instance, but an opaque address.
  // This prevents us from touching the class itself. We can only send messages to it
  // and we don't assume it is even on the same machine.
  val videoIdToActorRef = mutable.Map.empty[String, ActorRef]
  import context.dispatcher

  // This implicit determines the timeout for the "ask pattern" we use below. We will learn
  // more about implicits in the next session.
  implicit val timeout = Timeout(10.seconds)

  override def receive: Receive = {
    case RegisterNewVideoRequest(videoId) =>
      // Launch a new actor
      val videoActor: ActorRef = context.actorOf(Props[VideoActor], videoId)

      // Add it to our map
      videoIdToActorRef += (videoId -> videoActor)

    case ForwardVideoRequest(videoId, request) =>
      // Find the video actor reference from the map (let's assume the id is there)
      // and forward the request to it
      videoIdToActorRef(videoId).forward(request)

    case AggregateTrendsRequest =>
      // Ask all actors for their documents
      val documentFutures: Vector[Future[ReplyDocument]] = videoIdToActorRef.values.toVector.map {
        ref => ref.ask(GetDocumentRequest).mapTo[ReplyDocument]
      }

      // So we have an iterable of futures. We have seen in Futures.scala (the previous exercise)
      // how to convert a collection of futures to a future of collections.
      val futureDocuments: Future[Vector[ReplyDocument]] = ??? // documentFutures.

      // Now lets get the trend vectors from each document
      // Hint: you need to use map twice (the first to get inside the future, and the other to map
      // over the sequence that's inside). You can't use nested for loop since we are not flat-mapping on
      // the outer loop.
      val futureTrends: Future[Vector[Vector[Int]]] = futureDocuments.map(???)

      // Now let's flatten this so contatenate the trends
      val futureAggregatedTrends: Future[Vector[Int]] = futureTrends.map(???)

      val futureReply: Future[AggregatedTrendsReply] = futureAggregatedTrends.map(AggregatedTrendsReply(_))

      // pipeTo sends a future to another actor when the future is ready.
      futureReply.pipeTo(sender())
  }
}

// Time to run our code!
object VideoServiceMain {

  def test(actorSystem: ActorSystem) = {
    val service: ActorRef = actorSystem.actorOf(Props[VideoService], "videoservice")
    implicit val timeout = Timeout(10.seconds)

    service ! RegisterNewVideoRequest("ytv_v1")
    service ! RegisterNewVideoRequest("ytv_v2")
    service ! RegisterNewVideoRequest("ytv_v3")

    service ! ForwardVideoRequest("ytv_v1", UpdateDescriptionRequest("This is v1"))
    service ! ForwardVideoRequest("ytv_v2", UpdateDescriptionRequest("This is v2"))
    service ! ForwardVideoRequest("ytv_v3", UpdateDescriptionRequest("This is v3"))

    service ! ForwardVideoRequest("ytv_v1", AssociateCreatorRequest("creator1"))
    service ! ForwardVideoRequest("ytv_v2", AssociateCreatorRequest("creator1"))
    service ! ForwardVideoRequest("ytv_v3", AssociateCreatorRequest("creator1"))

    service ! ForwardVideoRequest("ytv_v1", DisassociateCreatorRequest("creator1"))
    // This should not disassociate
    service ! ForwardVideoRequest("ytv_v2", DisassociateCreatorRequest("creator2"))

    val v1Doc = Await.result(
      service.ask(ForwardVideoRequest("ytv_v1", GetDocumentRequest)).mapTo[ReplyDocument], Duration.Inf
    )

    val v2Doc = Await.result(
      service.ask(ForwardVideoRequest("ytv_v2", GetDocumentRequest)).mapTo[ReplyDocument], Duration.Inf
    )

    val v3Doc = Await.result(
      service.ask(ForwardVideoRequest("ytv_v3", GetDocumentRequest)).mapTo[ReplyDocument], Duration.Inf
    )

    println(s"v1Doc = ${v1Doc}")
    assert(v1Doc == ReplyDocument("This is v1", None, Vector()))
    println(s"v2Doc = ${v2Doc}")
    assert(v2Doc == ReplyDocument("This is v2", Some("creator1"), Vector()))
    println(s"v3Doc = ${v3Doc}")
    assert(v3Doc == ReplyDocument("This is v3", Some("creator1"), Vector()))

    println("Sending some trends")
    service ! ForwardVideoRequest("ytv_v1", AddTrendPointRequest(1))
    service ! ForwardVideoRequest("ytv_v1", AddTrendPointRequest(2))
    service ! ForwardVideoRequest("ytv_v1", AddTrendPointRequest(4))
    service ! ForwardVideoRequest("ytv_v2", AddTrendPointRequest(8))
    service ! ForwardVideoRequest("ytv_v2", AddTrendPointRequest(16))
    service ! ForwardVideoRequest("ytv_v3", AddTrendPointRequest(32))

    val trends = Await.result(service.ask(AggregateTrendsRequest).mapTo[AggregatedTrendsReply], Duration.Inf)
    println(s"Aggregated trends: ${trends}")
    assert(trends.trends.length == 6)
    assert(trends.trends.sum == 63)

    println(s"${Console.GREEN}**** SUCCESS! All assertions passed! ***")
    println("****")
    println(s"**** You have implemented a video service with no shared state!${Console.WHITE}")
  }

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()
    try {
      test(actorSystem)
      println("Hit enter to exit...")
      StdIn.readLine()
    } finally {
      actorSystem.terminate()
    }
  }
}
