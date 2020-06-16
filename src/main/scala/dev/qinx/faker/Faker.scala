package dev.qinx.faker

import java.lang.annotation.Annotation
import java.lang.reflect.Constructor
import java.time.LocalDate

import dev.qinx.faker.enums.Local
import dev.qinx.faker.internal.{CanProvide, HasSeed, HasString, Logging}
import dev.qinx.faker.utils.{DefaultProvider, ReflectUtils}

import scala.collection.mutable
import scala.reflect.ClassTag

class Faker[T: ClassTag](val local: Local) extends HasSeed with Logging {

  def this() = this(Local.en)

  private[this] val classTag = implicitly[ClassTag[T]]

  /**
   * The primary constructor of the class T
   */
  private[this] val primaryConstructor: Constructor[T] = {
    val constructor = classTag.runtimeClass.getDeclaredConstructors.head
    constructor.setAccessible(true)
    constructor.asInstanceOf[Constructor[T]]
  }

  /**
   * The list of provider for each parameter of the primary constructor.
   */
  private[this] lazy val providers: mutable.LinkedHashMap[String, CanProvide] = this.getProviders

  /**
   * Get an object of type T with faked data
   * @return an object of type T
   */
  def get(): T = primaryConstructor.newInstance(getInitArgs: _*)

  /**
   * Get a sequence of object T with faked data
   * @param length length of the output sequence
   * @return a seq of object T
   */
  def get(length: Long): Seq[T] = (1L to length).map(_ => get())

  /**
   * For a given annotation,
   * @param annotation
   * @throws
   * @return
   */
  @throws[NoSuchMethodException]
  private[this] def getProviderFromAnnotation(annotation: Annotation): CanProvide = {
    val provider = ReflectUtils
      .invokeAnnotationMethod[Class[CanProvide]](annotation, "provider")
      .newInstance()
      .configure(annotation)
    provider
  }

  /**
   * If a seed is set in Faker, then try to set the seed for the given provider if it has seed
   * @param provider provider that we want to set seed to
   */
  private[this] def setSeedOfProvider(provider: CanProvide): Unit = {
    this.seed match {
      case Some(seed) =>
        if (classOf[HasSeed].isAssignableFrom(provider.getClass)) {
          if (log.isTraceEnabled()) log.trace(s"${provider.getClass.getCanonicalName} has seed")
          provider.asInstanceOf[HasSeed].setSeed(seed)
        }
      case _ =>
    }
  }

  /**
   * Get the data providers for each parameter of the primary constructor
   * @return a Map of parameter name to its data provider
   */
  @throws[NoSuchElementException]("No provider could be found")
  @throws[NoSuchMethodException]
  private[this] def getProviders: mutable.LinkedHashMap[String, CanProvide] = {

    this.seed match {
      case Some(seed) => log.info(s"Set seed to $seed")
      case _ =>
    }

    val providers = new mutable.LinkedHashMap[String, CanProvide]()

    primaryConstructor.getParameters.foreach { p =>
      val annotation = p.getAnnotations.find { a =>
        ReflectUtils.hasDeclaredMethod(a.annotationType(), "provider")
      }

      annotation match {
        case Some(anno) =>
          log.debug(s"Set ${anno.annotationType().getSimpleName} provider for the field ${p.getName}: ${p.getType.getCanonicalName}")
          val provider = getProviderFromAnnotation(anno)
          setSeedOfProvider(provider)
          providers.put(p.getName, provider)
        case _ =>
          log.debug(s"Set default provider for the field ${p.getName}: ${p.getType.getCanonicalName}")
          val defaultProvider = DefaultProvider.of(p.getType)
          setSeedOfProvider(defaultProvider)
          providers.put(p.getName, defaultProvider)
      }
    }

    providers
  }

  /**
   * Generate the initial arguments for the primary constructor of the type T
   * @return an array of Object
   */
  private[this] def getInitArgs: Array[Object] = {
    primaryConstructor.getParameters.map { param =>
      val paramType = param.getType
      val provider = providers.getOrElse(param.getName, throw new NoSuchElementException(s"Cannot find provider of type ${param.getType}"))

      if (log.isTraceEnabled()) {
        log.trace(s"Generate fake data for ${param.getName}")
      }

      val fakeData = provider.provide()
      val fakeDataCls: Class[_] = fakeData.getClass
      val paramCls: Class[_] = ReflectUtils.getClassOf(paramType)

      if (paramCls.isAssignableFrom(fakeDataCls)) {
        fakeData.asInstanceOf[Object]
      } else {
        if (paramType.equals(classOf[String]) && classOf[HasString].isAssignableFrom(provider.getClass)) {
          provider.asInstanceOf[HasString].provideString.asInstanceOf[Object]
        } else {
          throw new NoSuchElementException(s"Cannot find provider of type ${param.getType}")
        }
      }

    }
  }
}

object Faker {

  def localDate(): LocalDate = {
    new dev.qinx.faker.provider.datetime.LocalDateProvider().provide()
  }

  def name(local: Local = Local.en): String = {
    dev.qinx.faker.provider.person.Name(local).provide()
  }


}
