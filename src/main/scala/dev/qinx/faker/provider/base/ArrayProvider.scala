package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{CanProvide, HasSeed, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.{DefaultProvider, ReflectUtils}

class ArrayProvider extends Provider[Object] with Logging with HasSeed {

  private[this] var provider: Option[CanProvide] = None
  private[this] var clsName: Option[Class[_]] = None
  private[this] var length: Int = 3

  def setArrayType(cls: Class[_]): this.type = {
    debug(s"Set ${cls.getCanonicalName} to array provider")
    this.clsName = Option(cls)
    this.provider = Option(DefaultProvider.of(cls.getComponentType))
    this
  }

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

    require(clsName.isDefined, "The array type is not set, set it with setArrayType()")
    require(provider.isDefined, "No component provider is set")
    trace(s"This array provider will provide $length instances of ${clsName.get.getComponentType}")

    val arr = java.lang.reflect.Array.newInstance(clsName.get.getComponentType, length)
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
