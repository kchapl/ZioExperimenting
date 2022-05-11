package zioexperimenting

import zio._

object LayeringApp extends ZIOAppDefault {

  case class A(id: Int)

  val layerA: ZLayer[Any, Nothing, A] = ZLayer.succeed(A(1))

  trait B {
    val s: ZIO[Any, Nothing, String]
  }

  val effect: ZIO[A, Nothing, String] = ZIO.succeed("str")

  val layerB: ZLayer[A, Nothing, B] = ZLayer.fromZIO(
    for {
      x <- ZIO.service[A]
    } yield new B {
      override val s: ZIO[Any, Nothing, String] = effect.provide(ZLayer.succeed(x))
    },
  )

  override def run: ZIO[ZIOAppArgs, Any, Any] =
    (for {
      s <- ZIO.serviceWithZIO[B](_.s)
      _ <- Console.printLine(s)
    } yield ()).provide(layerA, layerB)
}
