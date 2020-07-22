package dev.qinx.faker.provider.transport

import dev.qinx.faker.Faker

class Transport(val seed: Option[Long] = None) {

  def airport(): String = {
    val providerID = s"AirportProvider${seed.getOrElse("")}"
    Faker.provide[String](providerID) {
      id => new AirportProvider().setSeed(seed).setProviderID(id)
    }
  }

}
