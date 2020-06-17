package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class FloatProvider extends Provider[Float] with HasRandom with HasString with Logging {

  private[this] var min: Float = Float.MinValue
  private[this] var max: Float = Float.MaxValue

  def setMin(min: Float): this.type = {
    this.min = min
    this
  }

  def setMax(max: Float): this.type = {
    this.max = max
    this
  }

  override def provide(): Float = min + random.nextFloat() * (max - min)

  override def configure(annotation: Annotation): this.type = {
    this.setMax(ReflectUtils.invokeAnnotationMethod[Float](annotation, "max"))
    this.setMin(ReflectUtils.invokeAnnotationMethod[Float](annotation, "min"))
    this
  }

  override def provideString: String = provide().toString
}
