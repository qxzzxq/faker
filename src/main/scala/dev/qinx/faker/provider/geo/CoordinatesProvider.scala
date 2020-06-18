package dev.qinx.faker.provider.geo

import java.lang.annotation.Annotation

import dev.qinx.faker.annotation.geo.{Lat, Lon}
import dev.qinx.faker.provider.base.DoubleProvider
import dev.qinx.faker.utils.ReflectUtils

class CoordinatesProvider extends DoubleProvider {

  override def configure(annotation: Annotation): CoordinatesProvider.this.type = {

    assert(annotation.annotationType().equals(classOf[Lat]) || annotation.annotationType().equals(classOf[Lon]))
    
    val min = ReflectUtils.invokeAnnotationMethod[Double](annotation, "min")
    val max = ReflectUtils.invokeAnnotationMethod[Double](annotation, "max")

    if (annotation.annotationType().equals(classOf[Lat])) {
      require(min >= -90D && min <= 90D, "The lower bound of latitude should be between -90 and 90")
      require(max >= -90D && max <= 90D, "The upper bound of latitude should be between -90 and 90")
    } else if (annotation.annotationType().equals(classOf[Lon])) {
      require(min >= -180D && min <= 180D, "The lower bound of longitude should be between -180 and 180")
      require(max >= -180D && max <= 180D, "The upper bound of longitude should be between -180 and 180")
    }

    this.setMinMaxWithAnnotation(annotation)

    this
  }

}
