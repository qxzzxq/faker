package dev.qinx.faker.provider.datetime

import java.lang.annotation.Annotation
import java.time.{LocalDate => LD}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import dev.qinx.faker.internal.{HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class LocalDateProvider() extends Provider[LD] with HasRandom with HasString with Logging {

  private[this] var dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private[this] val minDay: LD = LD.of(1970, 1, 1)
  private[this] val range: Long = ChronoUnit.DAYS.between(minDay, LD.now())

  override def provide(): LD = {
    minDay.plusDays(random.nextInt(range.toInt))
  }

  override def configure(annotation: Annotation): this.type = {
    val pattern = ReflectUtils.invokeAnnotationMethod[String](annotation, "format")

    if (pattern != "") {
      log.debug(s"Set date pattern to $pattern")
      this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    }
    this
  }

  override def provideString: String = provide().format(dateTimeFormatter)
}
