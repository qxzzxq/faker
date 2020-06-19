package dev.qinx.faker.provider.datetime

import java.lang.annotation.Annotation
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import dev.qinx.faker.internal.{HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class LocalTimeProvider() extends Provider[LocalTime] with HasRandom with HasString with Logging {

  private[this] var dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_TIME
  private[this] val minTime: LocalTime = LocalTime.MIN
  private[this] val maxTime: LocalTime = LocalTime.MAX

  private[this] val range: Long = ChronoUnit.SECONDS.between(minTime, maxTime)

  def setPattern(pattern: String): this.type = {
    if (pattern != "") {
      log.debug(s"Set date pattern to $pattern")
      this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    }
    this
  }

  override def provide(): LocalTime = {
    minTime.plusSeconds(random.nextInt(range.toInt))
  }

  override def configure(annotation: Annotation): this.type = {
    val pattern = ReflectUtils.invokeAnnotationMethod[String](annotation, "format")
    this.setPattern(pattern)

    this
  }

  override def provideString: String = provide().format(dateTimeFormatter)
}
