package dev.qinx.faker.utils

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.Logging

object ReflectUtils extends Logging {

  def hasDeclaredMethod(cls: Class[_], name: String): Boolean = {
    try {
      cls.getDeclaredMethod(name)
      true
    } catch {
      case _: NoSuchMethodException => false
    }
  }

  /**
   * Invoke a method of the given class for an object of this class
   * @param cls class of obj
   * @param name name of the method
   * @param obj obj for which we invoke the method
   * @tparam T the output data type
   * @return an object of type T
   */
  def invokeMethod[T](cls: Class[_], name: String, obj: Object): T = {
    if (log.isTraceEnabled()) {
      log.trace(s"Invoke the method `$name` of the class ${cls.getCanonicalName}")
    }
    val method = cls.getDeclaredMethod(name)
    method.invoke(obj).asInstanceOf[T]
  }

  def getClassOf(cls: Class[_]): Class[_] = {
    cls.getName match {
      case "float" => classOf[java.lang.Float]
      case "double" => classOf[java.lang.Double]
      case "int" => classOf[java.lang.Integer]
      case "long" => classOf[java.lang.Long]
      case "short" => classOf[java.lang.Short]
      case "boolean" => classOf[java.lang.Boolean]
      case "char" => classOf[java.lang.Character]
      case "byte" => classOf[java.lang.Byte]
      case _ => cls
    }
  }

  def invokeAnnotationMethod[T](anno: Annotation, name: String): T = {
    invokeMethod[T](anno.annotationType(), name, anno)
  }

}
