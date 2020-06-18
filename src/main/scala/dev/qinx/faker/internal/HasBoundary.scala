package dev.qinx.faker.internal

import java.lang.annotation.Annotation

import dev.qinx.faker.utils.ReflectUtils


trait HasBoundary[T] {

  protected var min: T
  protected var max: T

  def setMin(min: T): this.type = {
    this.min = min
    this
  }

  def setMax(max: T): this.type = {
    this.max = max
    this
  }

  protected def setMinMaxWithAnnotation(annotation: Annotation): this.type = {
    this.setMax(ReflectUtils.invokeAnnotationMethod[T](annotation, "max"))
    this.setMin(ReflectUtils.invokeAnnotationMethod[T](annotation, "min"))
    this
  }

}
