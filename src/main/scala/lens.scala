package net.rosien.scalaz

object LensExamples {
  import scalaz._
  import Scalaz._

  // the data
  case class Foo(name: String, factor: Int)

  // a node of the tree
  case class FooNode(
    value: Foo,
    children: Seq[FooNode] = Seq())

  val tree =
    FooNode(
      Foo("root", 11),
      Seq(
        FooNode(Foo("child1", 1)),
        FooNode(Foo("child2", 2)))) // <-- * 4

  val secondTimes4: FooNode => FooNode =
    node => node.copy(children = {
      val second = node.children(1)
      node.children.updated(
        1,
        second.copy(
          value = second.value.copy(
            factor = second.value.factor * 4)))
    })

  val second: Lens[FooNode, FooNode] =
    Lens.lensu(
      (node, c2) => node.copy(
        children = node.children.updated(1, c2)),
      _.children(1))

  val value: Lens[FooNode, Foo] =
    Lens.lensu(
      (node, value) => node.copy(value = value),
      _.value)

  val factor: Lens[Foo, Int] =
    Lens.lensu(
      (foo, fac) => foo.copy(factor = fac),
      _.factor)

  val secondFactor =
    second andThen value andThen factor

   /* FooNode(
       Foo("root", 11),
       Seq(
         FooNode(Foo("child1", 1)),
         FooNode(Foo("child2", 2))))
                               ^
                               ^
                               ^  */
  secondFactor(tree)        // 2

   /* FooNode(
       Foo("root", 11),
       Seq(
         FooNode(Foo("child1", 1)),
         FooNode(Foo("child2", 2))))
                               ^
                               ^  */
  secondFactor.mod(        _ * 4, tree)
  /* FooNode(                  ^
       Foo("root", 11),        ^
       Seq(                    ^
         FooNode(Foo("child1", ^)),
         FooNode(Foo("child2", 8))))
   */
}