package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasBoundary, HasRandom, HasString, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

class IntProvider
  extends Provider[Int]
    with HasRandom
    with HasBoundary[Int]
    with HasString
    with Logging  {

  override protected var min: Int = Integer.MIN_VALUE
  override protected var max: Int = Integer.MAX_VALUE

  override def provide(): Int = random.ints(1, min, max).findFirst().getAsInt

  override def configure(annotation: Annotation): IntProvider.this.type = this.setMinMaxWithAnnotation(annotation)

  override def provideString: String = provide().toString
}
