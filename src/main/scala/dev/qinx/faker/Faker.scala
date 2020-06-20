package dev.qinx.faker

import java.time.LocalDate

import dev.qinx.faker.enums.Locale
import dev.qinx.faker.internal.{HasSeed, Logging}
import dev.qinx.faker.provider.base.ClassProvider

import scala.reflect.ClassTag

class Faker[T: ClassTag](val locale: Locale) extends HasSeed with Logging {

  def this() = this(Locale.en)
  private[this] val classTag = implicitly[ClassTag[T]]

  private[this] lazy val classProvider = new ClassProvider()
    .setClass(classTag.runtimeClass)
    .setSeed(this.seed)

  /**
   * Get an object of type T with faked data
   * @return an object of type T
   */
  def get(): T = classProvider.provide().asInstanceOf[T]

  /**
   * Get a sequence of object T with faked data
   * @param length length of the output sequence
   * @return a seq of object T
   */
  def get(length: Long): Seq[T] = (1L to length).map(_ => get())

  /**
   * Set seed for Faker. This seed will be applied to providers if no seed can be found in the annotation.
   * In other word, seed set in annotation arguments will override this seed.
   * @param seed seed
   * @return
   */
  override def setSeed(seed: Long): Faker.this.type = super.setSeed(seed)
}

object Faker {

  def localDate(): LocalDate = {
    new dev.qinx.faker.provider.datetime.LocalDateProvider().provide()
  }

  def name(locale: Locale = Locale.en): String = {
    dev.qinx.faker.provider.person.NameProvider(locale).provide()
  }

}
