package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class ByteArrayProvider extends Provider[Array[Byte]] with HasRandom with Logging  {

  override def provide(): Array[Byte] = {
    val buffer = new Array[Byte](10)
    random.nextBytes(buffer)
    buffer
  }

  override def configure(annotation: Annotation): this.type = this
  
}
