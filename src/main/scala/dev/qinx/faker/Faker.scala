package dev.qinx.faker

import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

import dev.qinx.faker.enums.Locale
import dev.qinx.faker.internal.{HasSeed, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.provider.base.ClassProvider
import dev.qinx.faker.provider.collection.ArrayProvider

import scala.reflect.ClassTag

class Faker[T: ClassTag](val locale: Locale) extends HasSeed with Logging {

  def this() = this(Locale.en)

  private[this] val classTag = implicitly[ClassTag[T]]

  private[this] lazy val classProvider = new ClassProvider()
    .setClass(classTag.runtimeClass)
    .setSeed(this.seed)

  /**
   * Get an object of type T with faked data
   *
   * @return an object of type T
   */
  def get(): T = classProvider.provide().asInstanceOf[T]

  /**
   * Get a sequence of object T with faked data
   *
   * @param length length of the output sequence
   * @return a seq of object T
   */
  def get(length: Long): Seq[T] = (1L to length).map(_ => get())

  /**
   * Set seed for Faker. This seed will be applied to providers if no seed can be found in the annotation.
   * In other word, seed set in annotation arguments will override this seed.
   *
   * @param seed seed
   * @return
   */
  override def setSeed(seed: Long): Faker.this.type = super.setSeed(seed)
}

object Faker extends Logging {

  private[this] var seed: Option[Long] = None

  private[this] val providers = new ConcurrentHashMap[String, Provider[_]]()

  private[this] def hasProvider(id: String): Boolean = providers.containsKey(id)

  /**
   * For a provider id and a partioal function that takes an ID to crate a provider, firstly check if
   * another provider with the given id was previously registered. If not then instantiate the provider
   * with the given id and save it into providers. Otherwise, fetch the registered provider and provide the object.
   *
   * @param id id of the provider
   * @param pf partial function that takes an ID to instantiate a provider
   * @tparam T type of the output
   * @return an object of type T
   */
  private[this] def provide[T](id: String)(pf: String => Provider[T]): T = {
    if (!hasProvider(id)) {
      debug(s"Register provider $id")
      providers.put(id, pf(id))
    }
    providers.get(id).provide().asInstanceOf[T]
  }

  def setSeed(seed: Long): this.type = {
    this.seed = Option(seed)
    this
  }

  def provide[T](provider: Provider[T]): T = provide[T](provider.providerID)(_ => provider)

  def localDate(pattern: String = ""): LocalDate = {
    val providerID = s"${pattern}LocalDateProvider"
    provide[LocalDate](providerID) { id =>
      new dev.qinx.faker.provider.datetime.LocalDateProvider()
        .setPattern(pattern)
        .setSeed(this.seed)
        .setProviderID(id)
    }
  }

  def name(locale: Locale = Locale.en): String = {
    val providerID = s"${locale.name()}NameProvider"
    provide[String](providerID) {
      id => dev.qinx.faker.provider.person
        .NameProvider(locale)
        .setProviderID(id)
    }
  }

  def array[T](length: Int)(implicit classTag: ClassTag[T]): Array[T] = {
    val providerID = s"${classTag.runtimeClass.getCanonicalName}${length}ArrayProvider"

    provide[Object](providerID) { id =>
      new ArrayProvider()
        .setProviderID(id)
        .setComponentType(classTag.runtimeClass)
        .setLength(length)
        .setSeed(seed)
    }.asInstanceOf[Array[T]]
  }

}
