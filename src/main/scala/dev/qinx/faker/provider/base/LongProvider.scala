package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasBoundary, HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider

class LongProvider
  extends Provider[Long]
    with HasRandom
    with HasString
    with HasBoundary[Long]
    with Logging  {

  override protected var min: Long = java.lang.Long.MIN_VALUE
  override protected var max: Long = java.lang.Long.MAX_VALUE

  override def provide(): Long = min + (random.nextDouble() * (max - min)).toLong

  override def configure(annotation: Annotation): this.type = this.setMinMaxWithAnnotation(annotation)

  override def provideString: String = provide().toString
}
