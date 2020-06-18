package dev.qinx.faker.provider.geo

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.geo.{Lat, Lon}
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class CoordinatesProviderSuite extends AnyFunSuite {
  import dev.qinx.faker.provider.geo.CoordinatesProviderSuite._

  test("CoordinatesProvider should provide lat/lon") {

    val faker = new Faker[TestCoordinatesProvider]
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())

    (1 to 100) foreach { _ =>
      val fakeData = faker.get()
      assert(fakeData.lat >= -90 && fakeData.lat <= 90)
      assert(fakeData.lon >= -180 && fakeData.lon <= 180)
    }

  }

  test("Set seed") {
    new SeedTester[TestCoordinatesProvider]
  }

  test("CoordinatesProvider exceptions") {
    assertThrows[IllegalArgumentException](new Faker[CoordinatesProviderExceptionLatMax].get())
    assertThrows[IllegalArgumentException](new Faker[CoordinatesProviderExceptionLatMin].get())
    assertThrows[IllegalArgumentException](new Faker[CoordinatesProviderExceptionLonMax].get())
    assertThrows[IllegalArgumentException](new Faker[CoordinatesProviderExceptionLonMin].get())
  }

}

object CoordinatesProviderSuite {

  case class TestCoordinatesProvider(@Lat lat: Double,
                                     @Lon lon: Double)

  case class CoordinatesProviderExceptionLatMax(@Lat(max = 200D) lat: Double)
  case class CoordinatesProviderExceptionLatMin(@Lat(min = -200D) lat: Double)
  case class CoordinatesProviderExceptionLonMax(@Lon(max = 200D) lat: Double)
  case class CoordinatesProviderExceptionLonMin(@Lon(min = -200D) lat: Double)
}


