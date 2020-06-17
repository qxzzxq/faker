package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider

class CharProvider extends Provider[Char] with HasRandom with Logging  {

  override def provide(): Char = {
    val low  = 33
    val high = 127
    (random.nextInt(high - low) + low).toChar
  }

  override def configure(annotation: Annotation): this.type = this
  
}
