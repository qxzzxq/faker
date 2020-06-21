package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{CanProvide, HasSeed, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.DefaultProvider

class ArrayProvider extends Provider[Object] with Logging with HasSeed {

  private[this] var provider: CanProvide = _
  private[this] var clsName: Class[_] = _
  private[this] var length: Int = 3

  def setArrayType(cls: Class[_]): this.type = {
    log.debug(s"Set ${cls.getCanonicalName} to array provider")
    this.clsName = cls
    this.provider = DefaultProvider.of(cls.getComponentType)
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

    if (classOf[HasSeed].isAssignableFrom(provider.getClass)) {
      if (log.isTraceEnabled()) {
        log.trace(s"${provider.getClass.getCanonicalName} can have seed")
      }
      // Do not override the existing seed
      if (!provider.asInstanceOf[HasSeed].hasSeed) {
        provider.asInstanceOf[HasSeed].setSeed(seed)
      }
    }

    super.setSeed(seed)
  }

  override def provide(): Object = {

    if (log.isTraceEnabled()) {
      log.trace(s"This array provider will provide $length instances of ${clsName.getComponentType}")
    }

    val arr = java.lang.reflect.Array.newInstance(clsName.getComponentType, length)
    (0 until length).foreach {i =>
      java.lang.reflect.Array.set(arr, i, provider.provide())
    }
    arr
  }

  override def configure(annotation: Annotation): ArrayProvider.this.type = this
}
