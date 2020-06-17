package dev.qinx.faker.utils

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.Logging

private[faker] object ReflectUtils extends Logging {

  /**
   * Check if the class has a method that match the given name
   *
   * @param cls  class to be checked
   * @param name name of the method to be checked
   * @throws NullPointerException if name is null
   * @throws SecurityException    f a security manager, s, is present and any of the conditions is met
   * @return true if the class has such a method
   */
  @throws[NullPointerException]
  @throws[SecurityException]
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
   *
   * @param cls  class of obj
   * @param name name of the method
   * @param obj  obj for which we invoke the method
   * @tparam T the output data type
   * @throws NoSuchMethodException  if a matching method is not found.
   * @throws IllegalAccessException if the method is an instance method and the specified object argument is not
   *                                an instance of the class or interface declaring the underlying method (or of
   *                                a subclass or implementor thereof); if the number of actual and formal
   *                                parameters differ; if an unwrapping conversion for primitive arguments fails;
   *                                or if, after possible unwrapping, a parameter value cannot be converted to the
   *                                corresponding formal parameter type by a method invocation conversion.
   * @return an object of type T
   */
  @throws[NoSuchMethodException]
  @throws[IllegalAccessException]
  def invokeMethod[T](cls: Class[_], name: String, obj: Object): T = {
    if (log.isTraceEnabled()) {
      log.trace(s"Invoke the method `$name` of the class ${cls.getCanonicalName}")
    }
    val method = cls.getDeclaredMethod(name)
    method.invoke(obj).asInstanceOf[T]
  }

  /**
   * Get the boxed class for the given class. If the input class is primitive, then return the boxed class
   *
   * @param cls input class
   * @return
   */
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

  /**
   * Invoke a method of the given annotation
   *
   * @param annotation annotation
   * @param name       name of the method
   * @tparam T type of the output
   * @throws NoSuchMethodException if a matching method is not found.
   * @return an object of type T
   */
  @throws[NoSuchMethodException]
  def invokeAnnotationMethod[T](annotation: Annotation, name: String): T = {
    invokeMethod[T](annotation.annotationType(), name, annotation)
  }

}
