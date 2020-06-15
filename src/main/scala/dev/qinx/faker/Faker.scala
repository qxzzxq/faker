package dev.qinx.faker

import java.lang.annotation.Annotation
import java.lang.reflect.{Constructor, Parameter}
import java.time.LocalDate

import dev.qinx.faker.enums.Local
import dev.qinx.faker.internal.{CanProvide, HasSeed, Logging}
import dev.qinx.faker.utils.ReflectUtils

import scala.collection.mutable
import scala.reflect.ClassTag

class Faker[T : ClassTag](val local: Local) extends HasSeed with Logging {

  def this() = this(Local.en_US)

  private[this] val classTag = implicitly[ClassTag[T]]

  private[this] val primaryConstructor: Constructor[T] = {
    val constructor = classTag.runtimeClass.getDeclaredConstructors.head
    constructor.setAccessible(true)
    constructor.asInstanceOf[Constructor[T]]
  }

  private[this] val providers: mutable.LinkedHashMap[String, CanProvide] = this.getProviders

  def get(): T = {
    primaryConstructor.newInstance(getInitArgs: _*)
  }

  def get(length: Long): Seq[T] = {
    (1L to length).par.map(_ => get()).seq
  }

  private[this] def getProvider(annotation: Annotation): CanProvide = {
    val method = annotation.annotationType().getDeclaredMethod("provider")
    val provider = method.invoke(annotation).asInstanceOf[Class[CanProvide]].newInstance()

    this.seed match {
      case Some(seed) => if (provider.isInstanceOf[HasSeed]) {
        log.debug("True")
      }
      case _ =>
        log.debug("No seed is set")
    }

    provider
  }

  private[this] def getProviders: mutable.LinkedHashMap[String, CanProvide] = {

    val providers = new mutable.LinkedHashMap[String, CanProvide]()
    primaryConstructor.getParameters.foreach { p =>
      val annotation = p.getAnnotations.find { a =>
        ReflectUtils.hasDeclaredMethod(a.annotationType(), "provider")
      }

      annotation match {
        case Some(anno) => providers.put(p.getName, getProvider(anno))
        case _ => throw new NoSuchElementException("")
      }
    }

    providers
  }

  private[this] def getInitArgs: Array[Object] = {
    providers.map { case (_, provider) =>
      provider.provide().asInstanceOf[Object]
    }.toArray
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
      override def provide(): LocalDate = new dev.qinx.faker.provider.LocalDateProvider().provide()
    }
  }
}
