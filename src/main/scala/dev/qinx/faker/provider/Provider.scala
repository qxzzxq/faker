package dev.qinx.faker.provider

import java.util.UUID

import dev.qinx.faker.internal.CanProvide

abstract class Provider[ProvidedT] extends CanProvide {
  private[this] var _providerID: String = UUID.randomUUID().toString

  private[faker] def providerID: String = _providerID

  private[faker] def setProviderID(id: String): this.type = {
    this._providerID = id
    this
  }

  override type T = ProvidedT
}
