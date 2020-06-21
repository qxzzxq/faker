package dev.qinx.faker.provider.transport

import java.lang.annotation.Annotation

import dev.qinx.faker.entity.Airport
import dev.qinx.faker.internal.{HasRandom, HasResource, HasString}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.{CSVReader, Constants, ReflectUtils}

import scala.collection.mutable.ArrayBuffer

class AirportProvider extends Provider[Airport] with HasResource with HasRandom with HasString {

  import AirportProvider._

  private[this] var country: Option[String] = None

  private[this] var airportTypes: Set[String] = Set(LARGE_AIRPORT, MEDIUM_AIRPORT)

  private[this] lazy val airportData: Array[Airport] = {
    val data = new CSVReader[Airport](s"${Constants.RESOURCE_DATA}/transport/airports.csv")
      .loadData
      .filter(a => airportTypes.contains(a.airportType))

    country match {
      case Some(c) => data.filter(a => a.countryCode == c)
      case _ => data
    }
  }

  override def provideString: String = provide().iataCode

  override def provide(): Airport = airportData(this.random.nextInt(airportData.length))

  override def configure(annotation: Annotation): AirportProvider.this.type = {

    val assertTrue: String => Boolean = { s =>
      ReflectUtils.invokeAnnotationMethod[String](annotation, s) == "true"
    }

    val apt: ArrayBuffer[String] = new ArrayBuffer[String]()

    if (assertTrue("largeAirport")) apt += LARGE_AIRPORT
    if (assertTrue("mediumAirport")) apt += MEDIUM_AIRPORT
    if (assertTrue("smallAirport")) apt += SMALL_AIRPORT
    if (assertTrue("heliport")) apt += HELIPORT

    ReflectUtils.invokeAnnotationMethod[String](annotation, "country") match {
      case "" =>
      case country => this.country = Option(country)
    }

    this.airportTypes = apt.toSet
    this.setSeed(annotation)

    this
  }
}

object AirportProvider {

  val LARGE_AIRPORT: String = "large_airport"
  val MEDIUM_AIRPORT: String = "medium_airport"
  val SMALL_AIRPORT: String = "small_airport"
  val HELIPORT: String = "heliport"

}
