package dev.qinx.faker.provider.collection

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasComponent, HasSeed}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

/**
 * Array provider will provide an array.
 *
 * {{{
 *   val p = new ArrayProvider
 *   p.setComponentType(classOf[String)
 *   p.provide().asInstanceOf[Array[String]]
 * }}}
 */
class ArrayProvider extends Provider[Object] with HasComponent with HasSeed {

  private[this] var length: Int = 3

  def setLength(length: Int): this.type = {
    this.length = length
    this
  }

  override def setSeed(seed: Long): ArrayProvider.this.type = {
    this.setSeed(Option(seed))
  }

  override def setSeed(seed: Option[Long]): ArrayProvider.this.type = {
    require(provider.isDefined, "No component provider is set")

    debug(s"Set array provider seed $seed")
    if (classOf[HasSeed].isAssignableFrom(provider.get.getClass)) {
      provider.get.asInstanceOf[HasSeed].setSeed(seed)
    }
    super.setSeed(seed)
  }

  override def provide(): Object = {

    require(componentType.isDefined, "The array type is not set, set it with setComponentType()")
    require(provider.isDefined, "No component provider is set")
    trace(s"This array provider will provide $length instances of ${componentType.get}")

    val arr = java.lang.reflect.Array.newInstance(componentType.get, length)
    (0 until length).foreach {i =>
      java.lang.reflect.Array.set(arr, i, provider.get.provide())
    }
    arr
  }

  override def configure(annotation: Annotation): ArrayProvider.this.type = {
    this.setLength(ReflectUtils.invokeAnnotationMethod[Int](annotation, "length"))
    val s = getSeedFromAnnotation(annotation)
    if (s.isDefined) {
      this.setSeed(s)
    }
    this
  }
}
