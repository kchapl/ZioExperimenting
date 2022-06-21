package zioexperimenting.service

import requests.Response
import zio._

trait Requests {
  def get(url: String): Task[Response]
}

object Requests {
  def get(url: String): RIO[Requests, Response] = ZIO.serviceWithZIO(_.get(url))
}

object RequestsLive {
  val layer: ULayer[Requests] =
    ZLayer.succeed(new Requests {
      override def get(url: String): Task[Response] =
        ZIO.attempt(requests.get(url))
    })
}
