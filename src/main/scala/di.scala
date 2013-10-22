package net.rosien.scalaz

object DependencyInjectionExamples {
  import scalaz._
  import Scalaz._

  trait SystemProperties {
    def get(key: String): Option[String]
  }

  object SystemProperties {
    def apply(m: Map[String, String]): SystemProperties =
      new SystemProperties {
        def get(key: String): Option[String] = m.get(key)
      }

    def default: SystemProperties = SystemProperties(sys.props.toMap)
  }

  case class Foo(name: String)
  case class Bar(count: Int)
  case class Baz(foo: Foo, bar: Bar)

  def mkFoo(props: SystemProperties): Option[Foo] =
    for {
      name <- props.get("foo.name")
    } yield Foo(name)

  def mkBar(props: SystemProperties): Option[Bar] =
    for {
      count <- props.get("bar.count")
    } yield Bar(count.toInt)

  def mkBaz(props: SystemProperties): Option[Baz] =
    for {
      foo <- mkFoo(props)
      bar <- mkBar(props)
    } yield Baz(foo, bar)

  val doBaz: Baz => Option[Unit] = baz => println(baz).some

  // hard-coded properties, bad!
  def doStuff0 = {
    val props = SystemProperties.default
    for {
      baz <- mkBaz(props)
    } yield doBaz(baz)
  }

  // extract val to method parameter
  def doStuff1(props: SystemProperties) =
    for {
      baz <- mkBaz(props)
    } yield doBaz(baz)

  // curry parameter
  val doStuff2: SystemProperties => Unit = props =>
    for {
      baz <- mkBaz(props)
    } yield doBaz(baz)

  import Kleisli.ask

  // replace curried parameter with ask
  val doStuff3: SystemProperties => Option[Unit] =
    ask[Option, SystemProperties] >=> Kleisli(mkBaz) >=> Kleisli(doBaz)

  val doStuff4: SystemProperties => Option[Unit] =
    ask[Option, SystemProperties] >==> mkBaz >==> doBaz

  type ReadProps[M[+_], A] = ReaderT[M, SystemProperties, A]

  val doStuff5: SystemProperties => Option[Unit] =
    for {
      props <- Kleisli.ask[Option, SystemProperties]
      baz   <- mkBaz(props).liftM[ReadProps]
      _     <- doBaz(baz).liftM[ReadProps]
    } yield ()

}
