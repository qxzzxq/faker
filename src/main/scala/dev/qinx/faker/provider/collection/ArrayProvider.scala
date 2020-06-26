package dev.qinx.faker.provider.collection

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{CanProvide, HasSeed, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.{DefaultProvider, ReflectUtils}

class ArrayProvider extends Provider[Object] with Logging with HasSeed {

  private[this] var provider: Option[CanProvide[_]] = None
  private[this] var componentType: Option[Class[_]] = None
  private[this] var length: Int = 3

  /**
   * Set the provider of the component of the array
   * @param provider a provider that can provide the array component
   * @return
   */
  def setProvider(provider: CanProvide[_]): this.type = {
    debug(s"Set array component provider ${provider.getClass.getCanonicalName}")
    this.provider = Option(provider)
    this
  }

  /**
   * Set the component type of this array provider. If no provider was set before calling this method, it will
   * set the default provider. Otherwise, it won't override the existing provider
   * @param componentType class of the component
   * @return
   */
  def setComponentType(componentType: Class[_]): this.type = {
    debug(s"Set array type to ${componentType.getCanonicalName}")
    this.componentType = Option(componentType)
    if (provider.isEmpty) {
      this.provider = Option(DefaultProvider.of(componentType))
    }
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
