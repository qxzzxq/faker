package dev.qinx.faker.provider.fallback

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider

class ShortProvider extends Provider[Short] with HasRandom with Logging  {

  override def provide(): Short = random.nextInt().toShort

  override def configure(annotation: Annotation): this.type = this

}
