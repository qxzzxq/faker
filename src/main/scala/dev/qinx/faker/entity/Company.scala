package dev.qinx.faker.entity

private[faker] final case class Company(code: String, name: String, sector: String) {
  def this(args: Array[String]) = this(args(0), args(1), args(2))
}
