package zioexperimenting

import zio._
import zio.test._

import java.time.{LocalDateTime, OffsetDateTime, ZoneOffset}

object TimeSpec extends ZIOSpecDefault {

  private val p =
    for {
      date <- Clock.currentDateTime.map(_.toLocalDate)
      _ <- Console.printLine(s"Date is $date")
    } yield ()

  override def spec = test("Testing p") {
    for {
      _ <- TestClock.setDateTime(
        OffsetDateTime.of(
          LocalDateTime.of(2022, 5, 21, 13, 41),
          ZoneOffset.ofHours(1),
        ),
      )
      _ <- p
      out <- TestConsole.output
    } yield assertTrue(out == Vector("Date is 2022-05-21\n"))
  }
}
