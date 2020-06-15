package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class LongProvider extends Provider[Long] with HasRandom with Logging  {

  override def provide(): Long = random.nextLong()

  override def configure(annotation: Annotation): this.type = this
  
}
