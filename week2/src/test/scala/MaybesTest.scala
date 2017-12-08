import org.scalatest.{FunSuite, MustMatchers, OptionValues}
import Maybes.Run

class MaybesTest extends FunSuite with MustMatchers with OptionValues {
  test("square(V) should square v") {
    Maybes.square(Vector.empty) must be(Vector.empty)
    Maybes.square(Vector(3,1,2)) must be(Vector(9,1,4))
  }

  test("means(V) should give the mean of v") {
    Maybes.mean(Vector.empty) must be(None)
    Maybes.mean(Vector(17.0)) must be(Some(17.0))
    Maybes.mean(Vector(5,7,9)) must be(Some(7.0))
  }

  test("averageAgeForName should work correctly") {
    Maybes.averageAgeForFirstName("Nadav") must be(None)
    Math.abs(Maybes.averageAgeForFirstName("John").value - 30.6666666) must be<=(0.001)
  }

  test("findPersonByFirstAndLast") {
    Maybes.findPersonByFirstAndLast("Nadav", "S") must be(None)
    Maybes.findPersonByFirstAndLast("Mike", "M").map(_.age).value must be(25)
    Maybes.findPersonByFirstAndLast("Mike", "C").map(_.age).value must be(55)
  }

  test("findHometownFirstAndLast") {
    Maybes.findHometownByFirstAndLast("Nadav", "S") must be(None)
    Maybes.findHometownByFirstAndLast("John", "D") must be(Some("San Francisco"))
    Maybes.findHometownByFirstAndLast("Mike", "M") must be(Some("Kiev"))
    Maybes.findHometownByFirstAndLast("Mike", "L") must be(None)
  }

  test("rleAppend") {
    Maybes.rleAppend(Vector.empty, 'd') must be(Vector(Run(1, 'd')))
    Maybes.rleAppend(Vector(Run(1, 'd')), 'd') must be(Vector(Run(2, 'd')))
    Maybes.rleAppend(Vector(Run(1, 'd')), 'e') must be(Vector(Run(1, 'd'), Run(1, 'e')))
    Maybes.rleAppend(Vector(Run(3, 'd')), 'd') must be(Vector(Run(4, 'd')))
    Maybes.rleAppend(Vector(Run(3, 'd')), 'e') must be(Vector(Run(3, 'd'), Run(1, 'e')))
    Maybes.rleAppend(Vector(Run(5, 'a'), Run(17, 'd')), 'a') must be(Vector(Run(5, 'a'), Run(17, 'd'), Run(1, 'a')))
    Maybes.rleAppend(Vector(Run(5, 'a'), Run(17, 'd')), 'd') must be(Vector(Run(5, 'a'), Run(18, 'd')))
    Maybes.rleAppend(Vector(Run(5, 'a'), Run(17, 'd')), 'e') must be(Vector(Run(5, 'a'), Run(17, 'd'), Run(1, 'e')))
  }

  test("compress") {
    Maybes.compress("aaa") must be (Vector(Run(3, 'a')))
    Maybes.compress("aaabbbb") must be (Vector(Run(3, 'a'), Run(4, 'b')))
    Maybes.compress("aaabbbbcc") must be (Vector(Run(3, 'a'), Run(4, 'b'), Run(2, 'c')))
  }

  test("uncompress") {
    Maybes.uncompress(Vector.empty) must be ("")
    Maybes.uncompress(Maybes.compress("aaa")) must be("aaa")
    Maybes.uncompress(Maybes.compress("aakkkvvzb")) must be("aakkkvvzb")
    Maybes.uncompress(Maybes.compress("aaa")) must be("aaa")
  }
}
