package net.rosien.scalaz

object MemoExamples {
  import scalaz._
  import Scalaz._

  case class Foo()
  case class Bar()

  def expensive(foo: Foo): Bar = {
    println("expensive: %s".format(foo))
    Bar()
  }

  def cache = collection.mutable.Map[Foo, Bar]()

  def memo: Foo => Bar =
    Memo.immutableHashMapMemo {
      foo: Foo => expensive(foo)
    }
}
