package dev.qinx.faker

import java.lang.annotation.Annotation
import java.lang.reflect.Constructor
import java.time.LocalDate

import dev.qinx.faker.enums.Local
import dev.qinx.faker.internal.{CanProvide, HasSeed, HasString, Logging}
import dev.qinx.faker.utils.{DefaultProvider, ReflectUtils}

import scala.collection.mutable
import scala.reflect.ClassTag

class Faker[T : ClassTag](val local: Local) extends HasSeed with Logging {

  def this() = this(Local.en)

  private[this] val classTag = implicitly[ClassTag[T]]

  private[this] val primaryConstructor: Constructor[T] = {
    val constructor = classTag.runtimeClass.getDeclaredConstructors.head
    constructor.setAccessible(true)
    constructor.asInstanceOf[Constructor[T]]
  }

  private[this] lazy val providers: mutable.LinkedHashMap[String, CanProvide] = this.getProviders

  def get(): T = primaryConstructor.newInstance(getInitArgs: _*)

  def get(length: Long): Seq[T] = (1L to length).map(_ => get())

  @throws[NoSuchMethodException]
  private[this] def getProvider(annotation: Annotation): CanProvide = {
    val provider = ReflectUtils
      .invokeMethod[Class[CanProvide]](annotation.annotationType(), "provider", annotation)
      .newInstance()
      .configure(annotation)

    this.seed match {
      case Some(seed) => if (classOf[HasSeed].isAssignableFrom(provider.getClass)) {
        provider.asInstanceOf[HasSeed].setSeed(seed)
      }
      case _ =>
    }
    provider
  }

  private[this] def getProviders: mutable.LinkedHashMap[String, CanProvide] = {

    this.seed match {
      case Some(seed) => log.debug(s"Set seed to $seed")
      case _ => log.debug("No seed is set")
    }

    val providers = new mutable.LinkedHashMap[String, CanProvide]()

    primaryConstructor.getParameters.foreach { p =>
      val annotation = p.getAnnotations.find { a =>
        ReflectUtils.hasDeclaredMethod(a.annotationType(), "provider")
      }

      annotation match {
        case Some(anno) =>
          log.debug(s"Set ${anno.annotationType().getSimpleName} provider for the field ${p.getName}: ${p.getType.getCanonicalName}")
          providers.put(p.getName, getProvider(anno))
        case _ =>
          log.debug(s"Set default provider for the field ${p.getName}: ${p.getType.getCanonicalName}")
          providers.put(p.getName, DefaultProvider.of(p.getType))
      }
    }

    providers
  }

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

  def apply[T](implicit provider: ImplicitProvider[T]): T = {
    provider.provide()
  }

  protected trait ImplicitProvider[T] {
    def provide(): T
  }

  object implicits {
    implicit val localDateProvider: ImplicitProvider[LocalDate] = new ImplicitProvider[LocalDate] {
      override def provide(): LocalDate = new dev.qinx.faker.provider.datetime.LocalDateProvider().provide()
    }
  }
}
