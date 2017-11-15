import org.scalatest.{FunSuite, MustMatchers}

class ListUtilsTest extends FunSuite with MustMatchers {
  test("isLengthAtLeast(L, 0) should be true for all lists L") {
    assert(ListUtils.isLengthAtLeast(List(), 0) === true)
    assert(ListUtils.isLengthAtLeast(List(1), 0) === true)
    assert(ListUtils.isLengthAtLeast(List(1,2), 0) === true)
    assert(ListUtils.isLengthAtLeast(List(1,2,3), 0) === true)
  }

  test("isLengthAtLeast(L, 1) should be true for all lists with more than one element") {
    assert(ListUtils.isLengthAtLeast(List(), 1) === false)
    assert(ListUtils.isLengthAtLeast(List(1), 1) === true)
    assert(ListUtils.isLengthAtLeast(List(1,2), 1) === true)
    assert(ListUtils.isLengthAtLeast(List(1,2,3), 1) === true)
  }

  test("isLengthAtLeast(L, 2) should be true for all lists with more than two element") {
    assert(ListUtils.isLengthAtLeast(List(), 2) === false)
    assert(ListUtils.isLengthAtLeast(List(1), 2) === false)
    assert(ListUtils.isLengthAtLeast(List(1, 2), 2) === true)
    assert(ListUtils.isLengthAtLeast(List(1, 2, 3), 2) === true)
  }

  test("isLengthAtLeast(L, i) should be true for all lists with more than i elements") {
    for {
      i <- 0 to 10
      j <- 0 to 10
    } {
      val listOfZeros = List.fill(i)(0)  // list with i zeros
      if (i >= j) {
        assert(ListUtils.isLengthAtLeast(listOfZeros, j) === true, s"Expected true for i=$i and j=$j")
      } else {
        assert(ListUtils.isLengthAtLeast(listOfZeros, j) === false, s"Expected false for i=$i and j=$j")
      }
    }
  }

  test("isSorted should return true for sorted lists") {
    assert(ListUtils.isSorted(List(1,2,3)) === true)
    assert(ListUtils.isSorted(List(1,2,3,4)) === true)
    assert(ListUtils.isSorted(List(-17, -12, 15)) === true)
    assert(ListUtils.isSorted((1 to 30).toList) === true)
  }

  test("isSorted should return true even if list is not strictly increasing") {
    assert(ListUtils.isSorted(List(1, 2, 2, 3)) === true)
  }

  test("isSorted should return false for non-sorted list of three elements") {
    assert(ListUtils.isSorted(List(3,1,2)) === false)
    assert(ListUtils.isSorted(List(-3, -6, -9)) === false)
  }

  test("isSorted should return true for the empty list") {
    assert(ListUtils.isSorted(List()) === true)
  }

  test("isSorted should return true for lists with one element") {
    assert(ListUtils.isSorted(List(3)) === true)
    assert(ListUtils.isSorted(List(-3)) === true)
    assert(ListUtils.isSorted(List(0)) === true)
  }


  test("isSorted should return true for sorted lists with two elements") {
    assert(ListUtils.isSorted(List(3, 4)) === true)
    assert(ListUtils.isSorted(List(-17, 9)) === true)
  }

  test("isSorted should return false for non-sorted lists with two elements") {
    assert(ListUtils.isSorted(List(4, 3)) === false)
    assert(ListUtils.isSorted(List(9, -17)) === false)
  }

}
