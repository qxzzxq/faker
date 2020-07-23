package dev.qinx.faker.provider.collection

import java.lang.annotation.Annotation
import java.util.concurrent.atomic.AtomicBoolean

import dev.qinx.faker.internal.{HasComponent, HasSeed}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

import scala.reflect.ClassTag

/**
 * Array provider will provide an array.
 *
 * {{{
 *   val p = new ArrayProvider
 *   p.setComponentType(classOf[String)
 *   p.provide().asInstanceOf[Array[String]]
 * }}}
 */
class ArrayProvider extends Provider[Array[_]] with HasComponent with HasSeed {

  private[this] var length: Int = 3
  private[this] val componentProviderSeedUpdated: AtomicBoolean = new AtomicBoolean(false)

  def setLength(length: Int): this.type = {
    this.length = length
    this
  }

  override def canProvide(cls: Class[_]): Boolean = {
    this.componentProvider.get.canProvide(cls.getComponentType)
  }

  override def getClassTag: ClassTag[Array[_]] = {
    ClassTag(this.componentProvider.get.getClassTag.newArray(0).getClass)
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

  override def provide(): Array[_] = {
    require(componentType.isDefined, "The array type is not set, set it with setComponentType()")
    require(componentProvider.isDefined, "No component provider is set")

    this.updateSeedOfComponentProvider()

    val _ct = componentType.get
    val _cp = componentProvider.get

    trace(s"This array provider will provide $length instances of ${_ct}")
    val arr = java.lang.reflect.Array.newInstance(_ct, length)
    (0 until length).foreach { i =>

      try {
        java.lang.reflect.Array.set(arr, i, _cp.provide())
      } catch {
        case _: IllegalArgumentException =>

          val arbitraryValue = ReflectUtils.provideArbitrary(_ct, _cp)

          arbitraryValue match {
            case Left(l) => java.lang.reflect.Array.set(arr, i, l)
            case Right(r) => java.lang.reflect.Array.set(arr, i, r)
            case _ =>
          }

        case e: Throwable => throw e
      }
    }
    arr.asInstanceOf[Array[_]]
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
