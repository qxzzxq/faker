package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasBoundary, HasOption, HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider

class FloatProvider
  extends Provider[Float]
    with HasRandom
    with HasString
    with HasBoundary[Float]
    with HasOption[Float]
    with Logging {

  override protected var min: Float = 0F
  override protected var max: Float = 1F

  override def provide(): Float = min + (random.nextDouble() * (max - min)).toFloat

  override def configure(annotation: Annotation): this.type = {
    this.setSeed(annotation)
    this.setMinMaxWithAnnotation(annotation)
  }

  override def provideString: String = provide().toString

  override def provideOption: Option[Float] = Option(provide())
}
