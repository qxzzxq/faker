package dev.qinx.faker.internal

import java.lang.annotation.Annotation

import dev.qinx.faker.utils.ReflectUtils


trait HasBoundary[BoundedT] {

  protected var min: BoundedT
  protected var max: BoundedT

  def setMin(min: BoundedT): this.type = {
    this.min = min
    this
  }

  def setMax(max: BoundedT): this.type = {
    this.max = max
    this
  }

  protected def setMinMaxWithAnnotation(annotation: Annotation): this.type = {
    this.setMax(ReflectUtils.invokeAnnotationMethod[BoundedT](annotation, "max"))
    this.setMin(ReflectUtils.invokeAnnotationMethod[BoundedT](annotation, "min"))
    this
  }

}
