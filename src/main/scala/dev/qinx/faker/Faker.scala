package dev.qinx.faker

import java.lang.annotation.Annotation
import java.lang.reflect.Constructor

import dev.qinx.faker.internal.CanProvide

import scala.reflect.ClassTag

class Faker[T : ClassTag](implicit classTag: ClassTag[T]) {

  private[this] val primaryConstructor: Constructor[T] = {
    val constructor = classTag.runtimeClass.getDeclaredConstructors.head
    constructor.setAccessible(true)
    constructor.asInstanceOf[Constructor[T]]
  }

  def get(): T = {
    primaryConstructor.newInstance(getInitArgs: _*)
  }

  def get(length: Long): Seq[T] = {
    (1L to length).par.map(_ => get()).seq
  }


  private[this] def getProvider(annotation: Annotation): CanProvide = {
    val method = annotation.annotationType().getDeclaredMethod("provider")
    val providerCls = method.invoke(annotation).asInstanceOf[Class[CanProvide]]
    providerCls.newInstance()
  }

  private[this] def getInitArgs: Array[Object] = {
    primaryConstructor.getParameters.map { p =>
      val annotation = p.getAnnotations.find {
        a =>
          try {
            a.annotationType().getDeclaredMethod("provider")
            true
          } catch {
            case _: NoSuchMethodException => false
          }
      }

      annotation match {
        case Some(anno) => getProvider(anno).provide().asInstanceOf[Object]
        case _ =>
          println(p.getParameterizedType)
          sys.exit()
      }
    }
  }
}
