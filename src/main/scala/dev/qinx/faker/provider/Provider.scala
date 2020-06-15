package dev.qinx.faker.provider

import dev.qinx.faker.internal.{CanProvide, HasSeed}

abstract class Provider[B] extends CanProvide with HasSeed {
  override type T = B
}
