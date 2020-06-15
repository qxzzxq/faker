package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class CharProvider extends Provider[Char] with HasRandom with Logging  {

  override def provide(): Char = random.nextPrintableChar()

  override def configure(annotation: Annotation): this.type = this
  
}
