package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal._
import dev.qinx.faker.provider.Provider

class IntProvider
  extends Provider[Int]
    with HasRandom
    with HasBoundary[Int]
    with HasOption[Int]
    with HasString
    with Logging  {

  override protected var min: Int = Integer.MIN_VALUE
  override protected var max: Int = Integer.MAX_VALUE
  private[this] lazy val range: Int = max - min

  override def provide(): Int = {
    // if overflow
    if (range < 0) {
      random.nextInt()
    } else {
      min + math.round(random.nextFloat() * range)
    }
  }

  override def configure(annotation: Annotation): IntProvider.this.type = {
    this.setSeed(annotation)
    this.setMinMaxWithAnnotation(annotation)
  }

  override def provideString: String = provide().toString

  override def provideOption: Option[Int] = Option(provide())
}
