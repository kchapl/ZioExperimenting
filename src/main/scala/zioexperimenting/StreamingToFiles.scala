package zioexperimenting

import zio._
import zio.stream.ZStream

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, Paths}
import java.nio.file.StandardOpenOption.CREATE_NEW

object StreamingToFiles extends ZIOAppDefault {

  private val numbers = 1 to 2500000
  private val batchSize = 10000

  private def toBytes(n: Int) =
    String.valueOf(s"$n\n").getBytes(UTF_8)

  private def write(content: Array[Byte], target: String) =
    ZIO.attempt(
      Files.write(
        Paths.get(target),
        content,
        CREATE_NEW,
      ),
    )

  private def writeToFile(ns: Chunk[Int]) =
    write(
      content = ns.flatMap(toBytes).toArray,
      target = s"out/${ns.head}-${ns.last}.txt",
    )

  private val program =
    ZStream
      .fromIterable(numbers)
      .grouped(batchSize)
      .mapZIOParUnordered(numbers.length / batchSize)(writeToFile)
      .foreach(_ => ZIO.unit)

  override def run: ZIO[ZIOAppArgs, Any, Any] =
    for {
      startTime <- Clock.currentDateTime
      _ <- Console.printLine(startTime)
      _ <- program
      endTime <- Clock.currentDateTime
      _ <- Console.printLine(endTime)
    } yield ()
}
