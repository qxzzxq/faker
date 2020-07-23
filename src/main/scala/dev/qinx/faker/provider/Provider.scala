package dev.qinx.faker.provider

import java.lang.annotation.Annotation
import java.util.UUID

import dev.qinx.faker.utils.ReflectUtils

import scala.reflect.ClassTag


abstract class Provider[ProvidedT: ClassTag]{
  private[this] val providedCls = implicitly[ClassTag[ProvidedT]]
  private[this] var _providerID: String = UUID.randomUUID().toString

  private[faker] def providerID: String = _providerID

  private[faker] def setProviderID(id: String): this.type = {
    this._providerID = id
    this
  }

  def provide(): ProvidedT

  def canProvide(cls: Class[_]): Boolean = {
    ReflectUtils.getClassOf(cls).isAssignableFrom(ReflectUtils.getClassOf(providedCls.runtimeClass))
  }

  def getClassTag: ClassTag[ProvidedT] = providedCls

  def configure(annotation: Annotation): Provider.this.type = {
    throw new NotImplementedError("To use a provider with annotation, the method `configure` must be override in the provider")
  }
}
