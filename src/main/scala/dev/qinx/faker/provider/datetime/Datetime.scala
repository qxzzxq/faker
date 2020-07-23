package dev.qinx.faker.provider.datetime

import java.time.{LocalDate, LocalDateTime, LocalTime}

import dev.qinx.faker.Faker

class Datetime(val seed: Option[Long] = None) {

  def localDate(): LocalDate = {
    val providerID = s"LocalDateProvider${seed.getOrElse("")}"
    Faker.provide[LocalDate](providerID) {
      id => new LocalDateProvider().setSeed(seed).setProviderID(id)
    }
  }

  def localDateTime(): LocalDateTime = {
    val providerID = s"LocalDateTimeProvider${seed.getOrElse("")}"
    Faker.provide[LocalDateTime](providerID) {
      id => new LocalDateTimeProvider().setSeed(seed).setProviderID(id)
    }
  }

  def localTime(): LocalTime = {
    val providerID = s"LocalTimeProvider${seed.getOrElse("")}"
    Faker.provide[LocalTime](providerID) {
      id => new LocalTimeProvider().setSeed(seed).setProviderID(id)
    }
  }

}
