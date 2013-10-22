package net.rosien.scalaz

object StyleExamples {
  import scalaz._
  import Scalaz._

  type A = Int
  type B = Int
  type C = Int

  val f: A => B = ???
  val g: B => C = ???

  // using temps:
  val a: A = ???
  val b = f(a)
  val c = g(b)

  // or via composition, which is a bit ugly:
  val c2 = g(f(a))

  // "unix-pipey", aka, the Thrush combinator
  val c3 = a |> f |> g



  val p: Boolean = ???

  // ternary-operator-ish
  p ? "yes" | "no" // if (p) "yes" else "no"

  val o: Option[String] = ???

  o | "meh"        // o.getOrElse("meh")


  // scala, bad default type inference!
  Some("foo")  // Some[String]
  None         // None.type

  // scalaz
  "foo".some   // Option[String]
  none         // Option[Nothing], oops!
  none[String] // Option[String]
}