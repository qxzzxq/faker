package dev.qinx.faker.provider.collection

import java.lang.annotation.Annotation
import java.util.concurrent.atomic.AtomicBoolean

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
  private[this] val componentProviderSeedUpdated: AtomicBoolean = new AtomicBoolean(false)

  def setLength(length: Int): this.type = {
    this.length = length
    this
  }


  /**
   * Check if this array provider has seed. If true then try to update the seed of the component provider.
   * But if the component provider already has a seed, then we skip the updating
   */
  private[this] def updateSeedOfComponentProvider(): Unit = {
    require(componentProvider.isDefined, "No component provider is set")

    if (classOf[HasSeed].isAssignableFrom(this.componentProvider.get.getClass) && !componentProviderSeedUpdated.getAndSet(true)) {
      val p = this.componentProvider.get.asInstanceOf[HasSeed]
      if (this.hasSeed && !p.hasSeed) {
        debug("Override component provider seed")
        p.setSeed(this.seed)
      } else {
        debug("Component provider already has a seed, do not override")
      }
    }
  }

  override def provide(): Object = {
    require(componentType.isDefined, "The array type is not set, set it with setComponentType()")
    require(componentProvider.isDefined, "No component provider is set")

    this.updateSeedOfComponentProvider()
    trace(s"This array provider will provide $length instances of ${componentType.get}")
    val arr = java.lang.reflect.Array.newInstance(componentType.get, length)
    (0 until length).foreach { i =>
      java.lang.reflect.Array.set(arr, i, componentProvider.get.provide())
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
