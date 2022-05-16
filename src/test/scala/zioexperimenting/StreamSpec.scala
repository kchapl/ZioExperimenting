package zioexperimenting

import zio._
import zio.stream.ZStream
import zio.test._

object StreamSpec extends ZIOSpecDefault {

  private val p = ZStream.iterate(1)(_ + 1).take(1000).foreach(Console.printLine(_))

  override def spec = test("Testing stream") {
    for {
      _ <- p
      out <- TestConsole.output
    } yield assertTrue(out.length == 1000)
  }
}
