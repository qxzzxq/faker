package dev.qinx.faker

import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

import dev.qinx.faker.enums.Locale
import dev.qinx.faker.internal.{HasSeed, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.provider.base.ClassProvider
import dev.qinx.faker.provider.collection.ArrayProvider
import dev.qinx.faker.provider.datetime.Datetime
import dev.qinx.faker.provider.person.Person
import dev.qinx.faker.provider.transport.Transport

import scala.reflect.ClassTag

final class Faker[T: ClassTag](val locale: Locale) extends HasSeed with Logging {

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
   * @param length totalLength of the output sequence
   * @return a seq of object T
   */
  def get(length: Long): Seq[T] = (1L to length).map(_ => get())

  /**
   * If the data has one or more series annotation, then return a seq with the minimum length that contains
   * all the unique series element combination
   *
   * @return
   */
  def getDataSeries: Seq[T] = {
    val first = get() // instantiate lazy variables
    val l = classProvider.getDataSeriesLength

    if (l != 0) {
      info(s"Get data series of length $l")
      first +: get(l - 1)
    } else {
      warn("The current case class cannot be provided as a data series")
      Seq()
    }
  }

  /**
   * Set seed for Faker. This seed will be applied to providers if no seed can be found in the annotation.
   * In other word, seed set in annotation arguments will override this seed.
   *
   * @param seed seed
   * @return
   */
  override def setSeed(seed: Long): Faker.this.type = super.setSeed(seed)

  def putSeries(id: String, data: Iterable[_]): this.type = {
    Faker.putSeries(id, data)
    this
  }

  def getSeries(id: String): Array[_] = Faker.getSeries(id)
}

object Faker extends Logging {

  private[this] var seed: Option[Long] = None

  private[this] val providers = new ConcurrentHashMap[String, Provider[_]]()

  private[faker] def hasProvider(id: String): Boolean = providers.containsKey(id)

  private[this] val seriesRegistry: ConcurrentHashMap[String, Array[_]] = new ConcurrentHashMap[String, Array[_]]()

  def putSeries(id: String, data: Iterable[_]): Unit = seriesRegistry.put(id, data.toArray)

  def getSeries(id: String): Array[_] = {
    if (seriesRegistry.containsKey(id)) {
      seriesRegistry.get(id)
    } else {
      throw new NoSuchElementException(s"Cannot find any series with id '$id'")
    }
  }

  /**
   * For a provider id and a partial function that takes an ID to crate a provider, firstly check if
   * another provider with the given id was previously registered. If not then instantiate the provider
   * with the given id and save it into providers. Otherwise, fetch the registered provider and provide the object.
   *
   * @param id id of the provider
   * @param pf partial function that takes an ID to instantiate a provider
   * @tparam T type of the output
   * @return an object of type T
   */
  private[faker] def provide[T : ClassTag](id: String)(pf: String => Provider[_])(implicit tag: ClassTag[T]): T = {

    saveProviderIfNotExist(id, pf)
    providers.get(id).provide().asInstanceOf[T]
  }

  private[this] def saveProviderIfNotExist(id: String, pf: String => Provider[_]): Unit = synchronized {
    if (!hasProvider(id)) {
      trace(s"Register provider $id")
      providers.put(id, pf(id))
    }
  }

  def setSeed(seed: Long): this.type = {
    this.seed = Option(seed)
    this
  }

  def provide[T: ClassTag](provider: Provider[T]): T = provide[T](provider.providerID)(_ => provider)

  def localDate(pattern: String = ""): LocalDate = {
    val providerID = s"${pattern}LocalDateProvider"
    provide[LocalDate](providerID) { id =>
      new dev.qinx.faker.provider.datetime.LocalDateProvider()
        .setPattern(pattern)
        .setSeed(this.seed)
        .setProviderID(id)
    }
  }

  def person(locale: Locale = Locale.en, seed: Option[Long] = None): Person = new Person(locale, seed)

  def transport(seed: Option[Long] = None): Transport = new Transport(seed)

  def datetime(seed: Option[Long] = None): Datetime = new Datetime(seed)

  def array[T](length: Int)(implicit classTag: ClassTag[T]): Array[T] = {
    val providerID = s"${classTag.runtimeClass.getCanonicalName}${length}ArrayProvider"

    provide[Object](providerID) { id =>
      new ArrayProvider()
        .setProviderID(id)
        .setComponentType(classTag.runtimeClass)
        .setLength(length)
        .setSeed(seed)
        .asInstanceOf[Provider[Object]]
    }.asInstanceOf[Array[T]]
  }

}
