package example

import zio._

import java.io.IOException
import java.time
import java.time.{Instant, LocalDateTime, OffsetDateTime}
import java.util.concurrent.TimeUnit

object Main extends ZIOAppDefault {

  private val program =
    for {
      time <- Clock.currentDateTime
      _ <- Console.printLine(time)
    } yield ()

  private val p = ZEnv.services.locallyWith(_.add(MyClock.clock).add(MyConsole.console))(program)

  override def run: ZIO[ZIOAppArgs, IOException, Unit] =
    p
}

object MyClock {
  val clock: Clock = new Clock {
    override def currentTime(unit: => TimeUnit)(implicit trace: Trace): UIO[Long] = ???

    override def currentDateTime(implicit trace: Trace): UIO[OffsetDateTime] =
      ZIO.succeed(OffsetDateTime.now.minusDays(30))

    override def instant(implicit trace: Trace): UIO[Instant] = ???

    override def localDateTime(implicit trace: Trace): UIO[LocalDateTime] = ???

    override def nanoTime(implicit trace: Trace): UIO[Long] = ???

    override def scheduler(implicit trace: Trace): UIO[Scheduler] = ???

    override def sleep(duration: => zio.Duration)(implicit trace: Trace): UIO[Unit] = ???

    def javaClock(implicit trace: Trace): UIO[time.Clock] = ???
  }
  val layer: ULayer[Clock] =
    ZLayer.succeed(clock)
}

object MyConsole {
  val console: Console = new Console {
    override def print(line: => Any)(implicit trace: Trace): IO[IOException, Unit] = ???

    override def printError(line: => Any)(implicit trace: Trace): IO[IOException, Unit] =
      ???

    override def printLine(line: => Any)(implicit trace: Trace): IO[IOException, Unit] =
      ZIO.succeed(println(s"OUTPUT: $line"))

    override def printLineError(line: => Any)(implicit
        trace: Trace,
    ): IO[IOException, Unit] = ???

    override def readLine(implicit trace: Trace): IO[IOException, String] = ???
  }

  val layer: ULayer[Console] = ZLayer.succeed(console)
}
