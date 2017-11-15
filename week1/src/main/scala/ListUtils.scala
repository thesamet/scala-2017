import scala.annotation.tailrec

object ListUtils {
  /** In Scala, a List[Int] represents a linked list of integers. Each node of
    * the linked list contains a value (head), and a reference to the rest of
    * the list (tail). It can be thought of something like this:
    *
    * class List[A] {
    *   def head: A           // returns the value of the given node
    *   def tail: List[A]     // returns a reference for the remainder of this list
    *   def isEmpty: Boolean  // true if this is the last element of the least
    * }
    *
    * As you can see from this definition, finding the length of a linked list requires
    * iterating through the entire list, until we reach the end of it (isEmpty returns false). It could
    * be implemented (inefficiently) like this:
    *
    * def length(l: List[Int]): Int = if (xs.isEmpty) 0 else (1 + length(xs.tail))
    *
    * Sometimes all we want to know is whether the list contains at least N elements, so
    * we don't have to iterate through the entire list.
    *
    * Exercise 1: implement a tail recursive function isLengthAtLeast(xs, k) that
    * takes a list xs and a value k and returns true if and only if the given list
    * has at least k elements.
    *
    * Of course, do not use the methods `size` or `length` as that would make your
    * implementation O(n) where n is the size of the list. We are looking for a solution
    * that takes at most k recursive calls.
    */
  @tailrec
  def isLengthAtLeast(xs: List[Int], k: Int): Boolean = {
    // Change this to a correct implementation
    false && isLengthAtLeast(Nil, k)
  }

  /** Exercise 2:
    *
    * Write a function that verifies that a given list of integers is sorted. By sorted,
    * we mean non-decreasing. In other words, each pair of consecutive items a and b, must satisfy
    * a < b.
    *
    * This function should also be tail recursive.
    *
    * Hint: to access the second element of a list, you can use xs(1) or xs.tail.head
    */
  @tailrec
  def isSorted(xs: List[Int]): Boolean = {
    // Change this to a correct implementation
    false && isSorted(Nil)
  }
}
