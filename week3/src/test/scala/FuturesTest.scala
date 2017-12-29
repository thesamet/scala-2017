import org.scalatest.{AsyncFlatSpec, MustMatchers}

class FuturesTest extends AsyncFlatSpec with MustMatchers {

  import Futures._

  behavior of "getName"

  it should "eventually return for John" in {
    getName(PersonId("j")).map { name => name must be("John") }
  }

  it should "eventually return for Alice" in {
    getName(PersonId("a")).map { name => name must be("Alice") }
  }

  it should "eventually throw a PersonNotFoundException" in {
    recoverToSucceededIf[PersonNotFoundException] {
      getName(PersonId("b"))
    }
  }

  behavior of "getChildId"

  it should "eventually return a person id of the child" in {
    getChildId(PersonId("a")).map { childId => childId must be(PersonId("j")) }
  }

  it should "throw a new NoChildException" in {
    recoverToSucceededIf[NoChildException] {
      getChildId(PersonId("j"))
    }
  }

  "getChild" should "eventually return John as the child of Alice" in {
    getChild(PersonId("a")).map { p => p.name must be("John") }
  }

  "getChild2" should "eventually return John as the child of Alice" in {
    getChild2(PersonId("a")).map { p => p.name must be("John") }
  }

  behavior of "getAllPersons"
  it should "return all persons" in {
    getAllPersons(Vector(PersonId("j"), PersonId("c"))).map {
      p => p.length must be(2)
    }
  }

  it should "throw when one of the persions does not exist" in {
    recoverToSucceededIf[PersonNotFoundException] {
      getAllPersons(Vector(PersonId("j"), PersonId("z")))
    }
  }

  behavior of "getAllPersons2"
  it should "return all persons" in {
    getAllPersons2(Vector(PersonId("j"), PersonId("c"))).map {
      p => p.length must be(2)
    }
  }

  it should "throw when one of the persions does not exist" in {
    recoverToSucceededIf[PersonNotFoundException] {
      getAllPersons2(Vector(PersonId("j"), PersonId("z")))
    }
  }

}
