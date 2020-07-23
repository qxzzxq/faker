package dev.qinx.faker.entity

private[faker] final case class Location(lat: Double, lon: Double, city: String, country: String, timeZone: String) {
  def this(args: Array[String]) = this(args(0).toDouble, args(1).toDouble, args(2), args(3), args(4))
}
