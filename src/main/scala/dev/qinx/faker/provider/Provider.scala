package dev.qinx.faker.provider

import java.lang.annotation.Annotation
import java.util.UUID

import dev.qinx.faker.internal.CanProvide

abstract class Provider[ProvidedT] extends CanProvide[ProvidedT] {
  private[this] var _providerID: String = UUID.randomUUID().toString

  private[faker] def providerID: String = _providerID

  private[faker] def setProviderID(id: String): this.type = {
    this._providerID = id
    this
  }

  override def configure(annotation: Annotation): Provider.this.type = {
    throw new NotImplementedError("To use a provider with annotation, the method `configure` must be override in the provider")
  }
}
