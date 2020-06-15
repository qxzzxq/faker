package dev.qinx.faker.utils

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

}
