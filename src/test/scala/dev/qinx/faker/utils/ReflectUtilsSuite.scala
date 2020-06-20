package dev.qinx.faker.utils

import dev.qinx.faker.annotation.person.Name
import org.scalatest.funsuite.AnyFunSuite

class ReflectUtilsSuite extends AnyFunSuite {

  import ReflectUtils._
  import ReflectUtilsSuite._

  test("ReflectUtils hasDeclaredMethod") {
    assert(hasDeclaredMethod(classOf[ReflectUtilsTestClass], "publicMethod"))
    assert(hasDeclaredMethod(classOf[ReflectUtilsTestClass], "privateMethod"))
  }

  test("ReflectUtils invoke methods") {
    val x = new ReflectUtilsTestClass("name")
    assert(invokeMethod[String](classOf[ReflectUtilsTestClass], "publicMethod", x) === "public")
    assertThrows[IllegalAccessException](invokeMethod[String](classOf[ReflectUtilsTestClass], "privateMethod", x))
    assertThrows[NoSuchMethodException](invokeMethod[String](classOf[ReflectUtilsTestClass], "noSuchMethod", x))
  }

  test("ReflectUtils get class") {
    val x = new ReflectUtilsTestClass("name")
    assert(!x.int.getClass.equals(classOf[java.lang.Integer]))
    assert(getClassOf(x.int.getClass).equals(classOf[java.lang.Integer]))
    assert(!getClassOf(x.int.getClass).equals(classOf[java.lang.Long]))
    assert(getClassOf(x.long.getClass).equals(classOf[java.lang.Long]))
    assert(getClassOf(x.float.getClass).equals(classOf[java.lang.Float]))
    assert(getClassOf(x.double.getClass).equals(classOf[java.lang.Double]))
    assert(getClassOf(x.char.getClass).equals(classOf[java.lang.Character]))
    assert(getClassOf(x.boolean.getClass).equals(classOf[java.lang.Boolean]))
    assert(getClassOf(x.byte.getClass).equals(classOf[java.lang.Byte]))
  }

  test("ReflectUtils invoke annotation method") {
    val annotation = classOf[ReflectUtilsTestClass].getConstructor(classOf[String]).getParameters.head.getAnnotations.head
    assert(invokeAnnotationMethod[String](annotation, "firstName") === "false")
  }



}

object ReflectUtilsSuite {

  class ReflectUtilsTestClass(@Name(firstName = "false") val name: String) {

    def publicMethod(): String = "public"
    private[this] def privateMethod(): String = "private"

    val int: Int = 1
    val long: Long = 1L
    val float: Float = 1F
    val double: Double = 1D
    val char: Char = "x"(0)
    val boolean: Boolean = true
    val byte: Byte = 1.byteValue()
  }

}
