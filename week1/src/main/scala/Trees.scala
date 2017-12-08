// In this exercise, we are defining a binary tree data structure.
//
// A trait is an abstract interface that may optionally contain
// implementations of some methods.
sealed trait Tree

// We now define two case classes that implement this interfaces:
case class Leaf(value: Int) extends Tree

case class Branch(left: Tree, right: Tree) extends Tree

object Trees {
  // Let's define some trees:
  val justZero = Leaf(0)

  val threeNodes = Branch(Leaf(1), Leaf(2))

  val twoLayers = Branch(
    Branch(
      Leaf(3),
      Leaf(4)),
    Branch(
      Leaf(5),
      Leaf(6)
    )
  )

  // We can create a new tree that references our other trees:
  val messyTree = Branch(
    twoLayers, Branch(Leaf(17), threeNodes)
  )

  // Exercise 1: write a function that sums the number of all values in the tree
  def sum(t: Tree): Int = t match {
    case Leaf(n) => 0
    case Branch(l, r) => 0
  }

  // Exercise 2: write a function that returns the number of nodes in the tree (all leafs and branches)
  def size(t: Tree): Int = ???

  // Exercise 3: write a function that applies a function f to each leaf in the tree.
  // The returned tree has the same shape as the given tree.
  //
  // For example. If our tree is Branch(Branch(Leaf(3), Leaf(5)), Leaf(9)):
  //
  //     -----+-----
  //     |         |
  //   --+--       9       and our function is (x: Int) => 2*x
  //   |   |
  //   3   5
  //
  // then the resulting tree should be:
  //
  //     -----+-----
  //     |         |
  //   --+--       18
  //   |   |
  //   6   10
  def map(t: Tree)(f: Int => Int): Tree = ???

  def main(args: Array[String]): Unit = {
    println(s"Sum of messyTree is ${sum(messyTree)} - expected 38")
    println(s"Size of messyTree is ${size(messyTree)} - expected 13")

    val modifiedTree = map(messyTree)(x => x*2)
    println(s"map(messyTree, _*2) is ${modifiedTree}")
    println(s"Expected sum is ${sum(modifiedTree)} - expected 76")

    val sumSquaredTree = sum(map(messyTree)(x => x*x))
    println(s"sum(map(messyTree, x => x*x)) is ${sumSquaredTree} - expected 380")
  }
}
