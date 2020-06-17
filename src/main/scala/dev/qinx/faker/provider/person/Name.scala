package dev.qinx.faker.provider.person

import java.lang.annotation.Annotation

import dev.qinx.faker.enums.Locale
import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class Name extends Provider[String] with HasRandom with Logging {

  private[this] var provider: LocalNameProvider = _

  override def provide(): String = this.provider.provide()

  override def setSeed(seed: Long): Name.this.type = {
    this.provider.setSeed(seed)
    this
  }

  override def configure(annotation: Annotation): this.type = {
    val locale = ReflectUtils.invokeAnnotationMethod[Locale](annotation, "locale")
    this.provider = Name(locale)
    this.provider.configure(annotation)
    this
  }

}

object Name {

  def apply(locale: Locale): LocalNameProvider = {
    val providerName = s"dev.qinx.faker.provider.person.${locale.name()}.NameProvider"

    Class
      .forName(providerName)
      .getDeclaredConstructors
      .head
      .newInstance()
      .asInstanceOf[LocalNameProvider]
  }

}
