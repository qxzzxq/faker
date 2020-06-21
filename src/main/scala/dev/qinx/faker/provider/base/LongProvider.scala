package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal._
import dev.qinx.faker.provider.Provider

class LongProvider
  extends Provider[Long]
    with HasRandom
    with HasString
    with HasOption[Long]
    with HasBoundary[Long]
    with Logging  {

  override protected var min: Long = java.lang.Long.MIN_VALUE
  override protected var max: Long = java.lang.Long.MAX_VALUE
  private[this] lazy val range: Long = max - min

  override def provide(): Long = {
    // if overflow
    if (range < 0) {
      random.nextLong()
    } else {
      min + math.round(random.nextDouble() * range)
    }
  }

  override def configure(annotation: Annotation): this.type = {
    this.setSeed(annotation)
    this.setMinMaxWithAnnotation(annotation)
  }

  override def provideString: String = provide().toString

  override def provideOption: Option[Long] = Option(provide())
}
