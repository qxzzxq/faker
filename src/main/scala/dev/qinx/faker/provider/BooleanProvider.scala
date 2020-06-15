package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class BooleanProvider extends Provider[Boolean] with HasRandom with Logging  {

  override def provide(): Boolean = random.nextBoolean()

  override def configure(annotation: Annotation): this.type = this
  
}
