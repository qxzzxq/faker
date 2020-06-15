package dev.qinx.faker.internal

trait CanProvide {

  type T

  def provide(): T

}
