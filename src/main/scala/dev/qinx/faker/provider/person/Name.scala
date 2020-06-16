package dev.qinx.faker.provider.person

import java.lang.annotation.Annotation
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import dev.qinx.faker.enums.Local
import dev.qinx.faker.internal.{HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class Name extends Provider[String] with HasRandom with Logging {

  private[this] var provider: LocalNameProvider = _

  override def provide(): String = this.provider.provide()

  override def configure(annotation: Annotation): this.type = {
    val local = ReflectUtils.invokeAnnotationMethod[Local](annotation, "local")
    val providerName = s"dev.qinx.faker.provider.person.${local.name()}.NameProvider"

    log.debug(s"Found local: $local}")
    log.debug(s"Provider name: $providerName}")

    this.provider = Class
      .forName(providerName)
      .getDeclaredConstructors
      .head
      .newInstance()
      .asInstanceOf[LocalNameProvider]

    this.provider.configure(annotation)
    this
  }

}
