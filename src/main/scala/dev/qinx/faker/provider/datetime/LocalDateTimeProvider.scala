package dev.qinx.faker.provider.datetime

import java.lang.annotation.Annotation
import java.time.{LocalDateTime, LocalTime}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import dev.qinx.faker.internal.{HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class LocalDateTimeProvider() extends Provider[LocalDateTime] with HasRandom with HasString with Logging {

  private[this] var dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
  private[this] val dateProvider = new LocalDateProvider
  private[this] val timeProvider = new LocalTimeProvider

  def setPattern(pattern: String): this.type = {
    if (pattern != "") {
      log.debug(s"Set datetime pattern to $pattern")
      this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    }
    this
  }

  override def setSeed(seed: Option[Long]): LocalDateTimeProvider.this.type = {
    dateProvider.setSeed(seed)
    timeProvider.setSeed(seed)
    this
  }

  override def setSeed(seed: Long): LocalDateTimeProvider.this.type = {
    this.setSeed(Some(seed))
  }

  override def provide(): LocalDateTime = {
    LocalDateTime.of(dateProvider.provide(), timeProvider.provide())
  }

  override def configure(annotation: Annotation): this.type = {
    val pattern = ReflectUtils.invokeAnnotationMethod[String](annotation, "format")
    this.setPattern(pattern)
    val _seed = super.getSeedFromAnnotation(annotation)
    if (_seed.isDefined) {
      this.setSeed(_seed)
    }

    this
  }

  override def provideString: String = provide().format(dateTimeFormatter)
}
