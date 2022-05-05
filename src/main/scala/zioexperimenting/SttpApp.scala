package zioexperimenting

import sttp.client3.httpclient.zio.{HttpClientZioBackend, SttpClient}
import sttp.client3.{basicRequest, UriContext}
import zio._

object SttpApp extends ZIOAppDefault {

  private val program =
    for {
      body <- Guardian.get("uk")
      _ <- Console.printLine(body)
    } yield ()

  override def run: ZIO[ZIOAppArgs, Any, Any] =
    program.provide(HttpClientZioBackend.layer(), GuardianLive.layer)
}

trait Guardian {
  def get(path: String): Task[String]
}

object Guardian {
  def get(path: String): RIO[Guardian, String] = ZIO.serviceWithZIO(_.get(path))
}

object GuardianLive {
  val layer: ZLayer[SttpClient, Throwable, Guardian] =
    ZLayer.fromZIO(
      for {
        sttpClient <- ZIO.service[SttpClient]
      } yield new Guardian {
        override def get(path: String): Task[String] =
          for {
            response <- sttpClient.send(basicRequest.get(uri"https://www.theguardian.com/$path"))
            body <- response.body match {
              case Left(failure) => ZIO.fail(new RuntimeException(failure))
              case Right(body)   => ZIO.succeed(body)
            }
          } yield body
      },
    )
}
