package dev.qinx.faker.provider

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import scala.util.Random

class LocalDateProvider() extends Provider[LocalDate] {

  private[this] val minDay: LocalDate = LocalDate.of(1970, 1, 1)
  private[this] val range: Long = ChronoUnit.DAYS.between(minDay, LocalDate.now())
  private[this] lazy val random: Random = {
    val r = Random
    seed match {
      case Some(s) => r.setSeed(s)
      case _ =>
    }
    r
  }

  override def provide(): LocalDate = {
    minDay.plusDays(random.nextInt(range.toInt))
  }

}
