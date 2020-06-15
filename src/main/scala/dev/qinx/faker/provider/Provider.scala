package dev.qinx.faker.provider

import dev.qinx.faker.internal.CanProvide

abstract class Provider[B] extends CanProvide {
  override type T = B
}
