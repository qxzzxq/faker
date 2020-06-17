package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class IntProvider extends Provider[Int] with HasRandom with HasString with Logging  {

  private[this] var min: Int = Integer.MIN_VALUE
  private[this] var max: Int = Integer.MAX_VALUE

  def setMin(min: Int): this.type = {
    this.min = min
    this
  }

  def setMax(max: Int): this.type = {
    this.max = max
    this
  }

  override def provide(): Int = random.ints(1, min, max).findFirst().getAsInt

  override def configure(annotation: Annotation): IntProvider.this.type = {
    this.setMax(ReflectUtils.invokeAnnotationMethod[Int](annotation, "max"))
    this.setMin(ReflectUtils.invokeAnnotationMethod[Int](annotation, "min"))
    this
  }

  override def provideString: String = provide().toString
}
