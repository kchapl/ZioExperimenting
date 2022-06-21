package zioexperimenting

import scala.annotation.tailrec

object Numeraliser extends App {

  def toRomanNumerals(num: Int): Option[String] = {
    @tailrec
    def go(n: Int, acc: String): Option[String] =
      if (n >= 1000) go(n - 1000, acc + 'M')
      else if (n >= 500) go(n - 500, acc + 'D')
      else if (n >= 100) go(n - 100, acc + 'C')
      else if (n >= 50) go(n - 50, acc + 'L')
      else if (n >= 10) go(n - 10, acc + 'X')
      else if (n >= 5) go(n - 5, acc + 'V')
      else if (n >= 1) go(n - 1, acc + 'I')
      else if (acc.isEmpty) None
      else Some(acc)

    def reduce(s: String): String =
      s
        .replace("IIII", "IV")
        .replace("VIV", "IX")
        .replace("XXXXIX", "IL")
        .replace("LIL", "IC")
        .replace("CCCCIC", "ID")
        .replace("DID", "IM")

    go(num, "").map(reduce)
  }

  val r = toRomanNumerals(1998)
  println(r)
}
