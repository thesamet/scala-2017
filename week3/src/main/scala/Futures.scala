import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

// PersonId is a case class that contains a string id of a person. Now
// we can pass PersonId instead of just Strings and we get more compile
// time safety!
case class PersonId(id: String)

case class Person(name: String, age: Int, childId: Option[PersonId])

class PersonNotFoundException(str: String) extends Exception

class NoChildException() extends Exception

object Futures extends App {
  def findPerson(id: PersonId) = Future {
    id match {
      case PersonId("j") => Person("John", 10, None)
      case PersonId("a") => Person("Alice", 39, Some(PersonId("j")))
      case PersonId("c") => Person("Carl", 40, None)
      case _ => throw new PersonNotFoundException("Error")
    }
  }

  // All the exercises below are meant to be solved by transforming Future objects.
  // You should never need to wait (block) until a future is complete to solve
  // these exercises.

  // Implement a function that returns a Future[String] representing
  // the name of the person with the given id.
  // Hint: you can map() over the result of findPerson
  def getName(id: PersonId): Future[String] = ???

  // Write a function that returns the person id of the child of the person.
  // If the child field is empty the future should fail with NoChildException.
  // Note that if you have an option x, you can use x.getOrElse(throw MyException())
  // Note - as you see in findPerson, throwing an exception inside a Future makes the future
  // fail.
  def getChildId(id: PersonId): Future[PersonId] = ???

  // Now let's combine two Futures. Write a function that returns the child of a person
  // given by id.
  // Hint: use flatMap.
  def getChild(id: PersonId): Future[Person] = ???

  // Now let's repeat the previous exercise. If you used flatMap, write it again
  // using a for-loop. If you used a for-loop solve the previous exercise with a flatMap.
  def getChild2(id: PersonId): Future[Person] = ???

  // flatMap lets us run two futures in sequence. In flatMap there is an assumption that
  // the second computation depends on the result of the first computation.
  // What if we want to run several futures in parallel?
  // In this exercise, we get a vector of person ids. We want to fetch all these persons
  // and return a future of Vector[Person]. This implies that if any of the queries fail
  // the whole result fails.
  def getAllPersons(personId: Vector[PersonId]): Future[Vector[Person]] = {
    // Futures begin running as soon as they are instantiated.
    // Let's create all of them by mapping over the persons
    val fs: Vector[Future[Person]] = personId.map(???)

    // We're getting close! We have Vector[Future[Person]] but we need Future[Vector[Person]] !
    // Big hint: try calling Future.sequence.
    ???
  }

  // Try writing this function again using Future.traverse
  // From the documentation of Future.traverse, it works like this:
  //
  //     val myFutureList = Future.traverse(myList)(x => Future(myFunc(x)))
  def getAllPersons2(personId: Vector[PersonId]): Future[Vector[Person]] = ???
}
