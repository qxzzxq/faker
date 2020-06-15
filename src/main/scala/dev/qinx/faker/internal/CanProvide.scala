package dev.qinx.faker.internal

trait CanProvide {

  type T

  def provide(): T

  def seed(seed: Long): this.type

}
