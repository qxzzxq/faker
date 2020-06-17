package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider

class LongProvider extends Provider[Long] with HasRandom with Logging  {

  override def provide(): Long = random.nextLong()

  override def configure(annotation: Annotation): this.type = this
  
}
