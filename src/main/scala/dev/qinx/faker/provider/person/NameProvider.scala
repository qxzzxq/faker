package dev.qinx.faker.provider.person

import java.lang.annotation.Annotation

import dev.qinx.faker.enums.Locale
import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.{Constants, ReflectUtils}

class NameProvider extends Provider[String] with HasRandom with Logging {

  private[this] var provider: LocalNameProvider = _

  override def provide(): String = this.provider.provide()

  override def setSeed(seed: Long): NameProvider.this.type = {
    if (!this.provider.hasSeed) {
      this.provider.setSeed(seed)
    }
    this
  }

  override def configure(annotation: Annotation): this.type = {
    val locale = ReflectUtils.invokeAnnotationMethod[Locale](annotation, "locale")
    this.provider = NameProvider(locale)
    this.provider.configure(annotation)
    this
  }

}

object NameProvider {

  def apply(locale: Locale): LocalNameProvider = {
    val providerName = s"${Constants.PERSON_PROVIDER_PACKAGE}.${locale.name()}.NameProvider"

    Class
      .forName(providerName)
      .getDeclaredConstructor()
      .newInstance()
      .asInstanceOf[LocalNameProvider]
  }

}
