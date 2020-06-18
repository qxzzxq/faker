package dev.qinx.faker.internal

trait HasOption[T] {

  def provideOption: Option[T]

}
