package dev.qinx.faker.utils

import java.time.LocalDate

import dev.qinx.faker.internal.CanProvide
import dev.qinx.faker.provider._
import dev.qinx.faker.provider.base._

object DefaultProvider {

  def of(cls: Class[_]): CanProvide = {
    if (cls.equals(classOf[LocalDate])) return new datetime.LocalDateProvider()
    if (cls.equals(classOf[String])) return new StringProvider()
    if (cls.equals(classOf[Float])) return new FloatProvider()
    if (cls.equals(classOf[Double])) return new DoubleProvider()
    if (cls.equals(classOf[Int])) return new IntProvider()
    if (cls.equals(classOf[Long])) return new LongProvider()
    if (cls.equals(classOf[Boolean])) return new BooleanProvider()
    if (cls.equals(classOf[Array[Byte]])) return new ByteArrayProvider()
    if (cls.equals(classOf[Short])) return new ShortProvider()
    if (cls.equals(classOf[Char])) return new CharProvider()
    if (cls.isArray) return new ArrayProvider().setArrayType(cls)
    new ClassProvider().setClass(cls)
  }

}
