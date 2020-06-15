package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class ShortProvider extends Provider[Short] with HasRandom with Logging  {

  override def provide(): Short = random.nextInt().toShort

  override def configure(annotation: Annotation): this.type = this

}
