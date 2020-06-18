package dev.qinx.faker.utils

import java.time.LocalDate

import dev.qinx.faker.internal.CanProvide
import dev.qinx.faker.provider._
import dev.qinx.faker.provider.base._

object DefaultProvider {

  @throws[NoSuchElementException]
  def of(obj: Class[_]): CanProvide = {
    if (obj.equals(classOf[LocalDate])) return new datetime.LocalDateProvider()
    if (obj.equals(classOf[String])) return new StringProvider()
    if (obj.equals(classOf[Float])) return new FloatProvider()
    if (obj.equals(classOf[Double])) return new DoubleProvider()
    if (obj.equals(classOf[Int])) return new IntProvider()
    if (obj.equals(classOf[Long])) return new LongProvider()
    if (obj.equals(classOf[Boolean])) return new BooleanProvider()
    if (obj.equals(classOf[Array[Byte]])) return new ByteArrayProvider()
    if (obj.equals(classOf[Short])) return new ShortProvider()
    if (obj.equals(classOf[Char])) return new CharProvider()
    new ClassProvider().setClass(obj)
  }

}
