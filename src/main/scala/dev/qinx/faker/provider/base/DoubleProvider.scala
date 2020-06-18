package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasBoundary, HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider

class DoubleProvider
  extends Provider[Double]
    with HasBoundary[Double]
    with HasString
    with HasRandom
    with Logging  {

  override protected var min: Double = 0D
  override protected var max: Double = 1D

  override def provideString: String = provide().toString

  override def provide(): Double = min + (random.nextDouble() * (max - min))

  override def configure(annotation: Annotation): this.type = this.setMinMaxWithAnnotation(annotation)
  
}
