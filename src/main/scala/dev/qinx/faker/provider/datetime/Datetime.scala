package dev.qinx.faker.provider.datetime

import java.time.LocalDate

import dev.qinx.faker.Faker

class Datetime(val seed: Option[Long] = None) {

  def localDate(): LocalDate = {
    val providerID = s"LocalDateProvider${seed.getOrElse("")}"
    Faker.provide[LocalDate](providerID) {
      id => new LocalDateProvider().setSeed(seed).setProviderID(id)
    }
  }

}
