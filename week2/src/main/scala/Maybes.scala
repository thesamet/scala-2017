object Maybes {
  // Let's explore Vectors and Options.  Feel free to use IntelliJ Cmd-B to jump to the source
  // of vectors and options when a cursor is pointing at some method.  You can find out more here:
  // Map: http://www.scala-lang.org/api/2.12.0/scala/collection/immutable/Map.html
  // Vector: http://www.scala-lang.org/api/2.12.0/scala/collection/immutable/Vector.html
  // Option: http://www.scala-lang.org/api/2.12.0/scala/Option.html

  // Exercise 1: Write a function that takes a vector and squares of its element.
  // For example, if the input is Vector(1.0, 3.0, 4.0) the result should be Vector(1.0, 9.0, 16.0)
  // Hint: use v.map
  def square(v: Vector[Double]): Vector[Double] = ???

  // Exercise 2: Write a function that takes a vector of doubles, and
  // returns the mean (average) of it. Sometimes the mean is undefined
  // so this function returns an Option[Double].  When the mean is defined it should
  // return Some(m) where m is the average, otherwise None.
  // Hint: In IntelliJ, type v. and see what methods are available.
  def mean(v: Vector[Double]): Option[Double] = ???

  case class Person(first: String, last: String, age: Double)

  val persons = Vector(
    Person("John",    "M", 44.0),
    Person("John",    "D", 17.0),
    Person("John",    "L", 31.0),
    Person("Mike",    "M", 25.0),
    Person("Mike",    "C", 55.0),
    Person("Barbara", "D", 29.0),
    Person("Moishe",  "Z", 33.0)
  )

  // Exercise 3: Write a function that returns the average age of all persons in `persons` that have
  // the given name.
  // Hint: use filter and map
  def averageAgeForFirstName(first: String): Option[Double] = ???

  // Exercise 4: Finds a person that has given first and last name.
  // Use persons.find() which takes a function that takes a person and returns a boolean.
  def findPersonByFirstAndLast(first: String, last: String): Option[Person] = ???

  // Let's pretend we have a function that knows how to find a person's hometown. It returns
  // an Option[String] since we don't have the data for everyone.
  def getHometown(p: Person): Option[String] = p.first match {
    case "John" => Some("San Francisco")
    case "Mike" => Some("Kiev")
    case _ => None
  }

  // Exercise 5: Let's write a function that returns a person hometown given his first and last name.
  // Hint: combine findPersonByFirstAndLast with getCity using a for-loop.
  def findHometownByFirstAndLast(first: String, last: String): Option[String] = ??? /* for {
    person <-
    hometown <-
  } yield ??? */

  // Exercise 6: RLE compression with foldLeft

  // A really important function in Functional Programming is foldLeft.
  // foldLeft allows us to iterate over a data structure while maintaining state.
  // For example, let's count the sevens in this vector:
  val t = Vector(4, 7, 1, 5, 7, 3)
  val numberOfSevens = t.foldLeft(0) { (acc, v) => if (v == 7) acc + 1 else acc }

  // foldLeft is taking two parameter lists. The first parameter is the initial value of the
  // state, which in this example is just 0. The second parameter is a function that computes a new
  // state given the current state and an element of the sequence. In this case, the state is: "how
  // many sevens have we seen so far"
  // Of course, this is just an example, an easier way to do the same would be to do
  // t.count(_ == 7)

  // RLE (run-length encoding) is a simple form of compression.  Let's say you want to compress
  // the string "aaabbccdddd". The compressed presentation can be:
  //   Vector(Run(3, 'a'), Run(2, 'b'), Run(4, 'd'))
  // That is, we have a run of three a's, followed by four d's.

  // The following represents a run of {count} times of the character {ch}.
  case class Run(count: Int, ch: Char)

  // Part 1: let's implement a function that takes a compressed representation of a string and a char, and
  // appends that char to the compressed presentation.
  // For example:
  //   rleAppend(Vector(Run(4, 'c')), 'c') == Vector(Run(5, 'c'))
  //   rleAppend(Vector(Run(4, 'c')), 'd') == Vector(Run(4, 'c'), Run(1, 'd'))
  // Hint:
  // v.last gives the last element, v.updates(index, r) will return an updated copy of the vector.
  def rleAppend(s: Vector[Run], ch: Char): Vector[Run] = ???

  // Part 2: use foldLeft and rleAppend to compress a string.
  // Vector.empty[Run] gives you an empty Vector of Runs.
  def compress(s: String): Vector[Run] = ???

  // Part 3: implement uncompress which takes a compressed string and recovers the original one
  // Hint: Vector[String] has a mkString method.
  // Hint: You can multiply a string and a number in Scala, but not a char and a number. To convert a character
  // to string, use its toString method.
  def uncompress(s: Vector[Run]): String = ???
}
