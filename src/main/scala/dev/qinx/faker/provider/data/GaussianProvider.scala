package dev.qinx.faker.provider.data

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasOption, HasRandom, HasString}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class GaussianProvider
  extends Provider[Double]
    with HasOption[Double]
    with HasString
    with HasRandom {

  private[this] var std: Double = 1D
  private[this] var mean: Double = 0D

  def setStandardDeviation(std: Double): this.type = {
    this.std = std
    this
  }

  def setMean(mean: Double): this.type = {
    this.mean = mean
    this
  }

  override def provideOption: Option[Double] = Option(this.provide())

  override def provideString: String = this.provide().toString

  override def provide(): Double = {
    this.random.nextGaussian() * std + mean
  }

  override def configure(annotation: Annotation): GaussianProvider.this.type = {
    val std = ReflectUtils.invokeAnnotationMethod[Double](annotation, "std")
    val mean = ReflectUtils.invokeAnnotationMethod[Double](annotation, "mean")

    this.setStandardDeviation(std)
    this.setMean(mean)
    this.setSeed(annotation)
    this
  }

}
