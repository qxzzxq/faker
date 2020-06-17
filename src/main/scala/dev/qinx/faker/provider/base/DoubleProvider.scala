package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider

class DoubleProvider extends Provider[Double] with HasRandom with Logging  {

  override def provide(): Double = random.nextDouble()

  override def configure(annotation: Annotation): this.type = this
  
}
