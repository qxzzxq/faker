package dev.qinx.faker.provider

import dev.qinx.faker.internal.CanProvide

abstract class Provider[ProvidedT] extends CanProvide {
  override type T = ProvidedT
}
