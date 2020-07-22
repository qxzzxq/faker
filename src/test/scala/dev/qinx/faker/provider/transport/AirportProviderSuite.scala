package dev.qinx.faker.provider.transport

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.transport.Airport
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class AirportProviderSuite extends AnyFunSuite {

  import AirportProviderSuite._

  test("Airport provider should provide airport and airport code") {
    val faker = new Faker[AirportProviderTest]
    println(faker.get())
    faker.get(1000)
  }

  test("Airport provider seed") {
    new SeedTester[AirportProviderTest]()
  }
}

object AirportProviderSuite {

  case class AirportProviderTest(@Airport airport: String,
                                 @Airport airportCode: String,
                                 @Airport(heliport = true, largeAirport = false) heliport: String,
                                 @Airport(smallAirport = true, largeAirport = false) small: String,
                                 @Airport(mediumAirport = true, largeAirport = false) medium: String,
                                 @Airport(mediumAirport = true) largeAndMedium: String,
                                 @Airport(country = "FR") fr: String
                                )

}
