package dev.qinx.faker.provider.transport

import java.lang.annotation.Annotation

import dev.qinx.faker.entity.Airport
import dev.qinx.faker.internal.{HasRandom, HasResource}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.{CSVReader, Constants, ReflectUtils}

import scala.collection.mutable.ArrayBuffer

class AirportProvider extends Provider[String] with HasResource with HasRandom {

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

  def setCountry(country: Option[String]): this.type = {
    this.country = country
    this
  }

  override def provide(): String = airportData(this.random.nextInt(airportData.length)).iataCode

  override def configure(annotation: Annotation): AirportProvider.this.type = {

    val getBoolean: String => Boolean = { s =>
      ReflectUtils.invokeAnnotationMethod[Boolean](annotation, s)
    }

    val apt: ArrayBuffer[String] = new ArrayBuffer[String]()

    if (getBoolean("largeAirport")) apt += LARGE_AIRPORT
    if (getBoolean("mediumAirport")) apt += MEDIUM_AIRPORT
    if (getBoolean("smallAirport")) apt += SMALL_AIRPORT
    if (getBoolean("heliport")) apt += HELIPORT

    ReflectUtils.invokeAnnotationMethod[String](annotation, "country") match {
      case "" =>
      case country => setCountry(Option(country))
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
