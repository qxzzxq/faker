package dev.qinx.faker.internal

trait HasOption[OptionT] {

  def provideOption: Option[OptionT]

}
