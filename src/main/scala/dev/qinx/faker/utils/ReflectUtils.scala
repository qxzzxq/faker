package dev.qinx.faker.utils

object ReflectUtils {

  def hasDeclaredMethod(cls: Class[_], name: String): Boolean = {
    try {
      cls.getDeclaredMethod(name)
      true
    } catch {
      case _: NoSuchMethodException => false
    }
  }

}
