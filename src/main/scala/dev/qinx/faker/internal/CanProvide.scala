package dev.qinx.faker.internal

import java.lang.annotation.Annotation

trait CanProvide[T] {

  def provide(): T

  def configure(annotation: Annotation): this.type

}
