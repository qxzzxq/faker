package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class FloatProvider extends Provider[Float] with HasRandom with Logging  {

  override def provide(): Float = random.nextFloat()

  override def configure(annotation: Annotation): this.type = this
  
}
