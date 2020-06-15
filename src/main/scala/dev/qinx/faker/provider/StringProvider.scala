package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class StringProvider extends Provider[String] with HasRandom with Logging {

  override def provide(): String = random.alphanumeric.take(10).mkString

  override def configure(annotation: Annotation): this.type = this
}
