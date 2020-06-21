package dev.qinx.faker.provider.transport

import dev.qinx.faker.annotation.transport.Airport
import dev.qinx.faker.utils.SeedTester
import dev.qinx.faker.{Faker, entity}
import org.scalatest.funsuite.AnyFunSuite

class AirportProviderSuite extends AnyFunSuite {

  import AirportProviderSuite._

  test("Airport provider should provide airport and airport code") {
    val faker = new Faker[AirportProviderTest]
    println(faker.get())
    faker.get(1000).foreach { a =>
      assert(a.airport.airportType === AirportProvider.LARGE_AIRPORT)
      assert(a.heliport.airportType === AirportProvider.HELIPORT)
      assert(a.small.airportType === AirportProvider.SMALL_AIRPORT)
      assert(a.medium.airportType === AirportProvider.MEDIUM_AIRPORT)
      assert(a.largeAndMedium.airportType === AirportProvider.MEDIUM_AIRPORT || a.largeAndMedium.airportType === AirportProvider.LARGE_AIRPORT)
      assert(a.fr.countryCode === "FR")
    }
  }

  test("Airport provider seed") {
    new SeedTester[AirportProviderTest]()
  }
}

object AirportProviderSuite {

  case class AirportProviderTest(@Airport airport: entity.Airport,
                                 @Airport airportCode: String,
                                 @Airport(heliport = "true", largeAirport = "false") heliport: entity.Airport,
                                 @Airport(smallAirport = "true", largeAirport = "false") small: entity.Airport,
                                 @Airport(mediumAirport = "true", largeAirport = "false") medium: entity.Airport,
                                 @Airport(mediumAirport = "true") largeAndMedium: entity.Airport,
                                 @Airport(country = "FR") fr: entity.Airport
                                )

}
