package dev.qinx.faker.provider.datetime

import java.lang.annotation.Annotation
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import dev.qinx.faker.internal.{HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class LocalDateProvider() extends Provider[LocalDate] with HasRandom with HasString with Logging {

  private[this] var dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
  private[this] val minDay: LocalDate = LocalDate.of(1970, 1, 1)
  private[this] val range: Long = ChronoUnit.DAYS.between(minDay, LocalDate.now())

  def setPattern(pattern: String): this.type = {
    if (pattern != "") {
      log.debug(s"Set date pattern to $pattern")
      this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    }
    this
  }

  override def provide(): LocalDate = {
    minDay.plusDays(random.nextInt(range.toInt))
  }

  override def configure(annotation: Annotation): this.type = {
    val pattern = ReflectUtils.invokeAnnotationMethod[String](annotation, "format")
    this.setPattern(pattern)
    this.setSeed(annotation)

    this
  }

  override def provideString: String = provide().format(dateTimeFormatter)
}
