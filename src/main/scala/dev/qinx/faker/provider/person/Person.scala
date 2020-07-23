package dev.qinx.faker.provider.person

import dev.qinx.faker.Faker
import dev.qinx.faker.enums.{Gender, Locale}

class Person(val locale: Locale = Locale.en,
             val seed: Option[Long] = None) {

  def lastName(): String = synchronized {
    val providerID = s"${locale.name()}${seed.getOrElse("")}LastNameProvider"
    Faker.provide[String](providerID) {
      id =>
        NameProvider(locale)
          .setProviderID(id)
          .setLastName(true)
          .setFirstName(false)
          .setGender(Gender.All)
          .setSeed(seed)
    }
  }

  def firstName(): String = synchronized {
    val providerID = s"${locale.name()}${seed.getOrElse("")}FirstNameProvider"
    Faker.provide[String](providerID) {
      id =>
        NameProvider(locale)
          .setProviderID(id)
          .setLastName(false)
          .setFirstName(true)
          .setGender(Gender.All)
          .setSeed(seed)
    }
  }

  def firstNameMale(): String = synchronized {
    val providerID = s"${locale.name()}${seed.getOrElse("")}firstNameMaleProvider"
    Faker.provide[String](providerID) {
      id =>
        NameProvider(locale)
          .setProviderID(id)
          .setLastName(false)
          .setFirstName(true)
          .setGender(Gender.Male)
          .setSeed(seed)
    }
  }

  def firstNameFemale(): String = synchronized {
    val providerID = s"${locale.name()}${seed.getOrElse("")}firstNameFemaleProvider"
    Faker.provide[String](providerID) {
      id =>
        NameProvider(locale)
          .setProviderID(id)
          .setLastName(false)
          .setFirstName(true)
          .setGender(Gender.Female)
          .setSeed(seed)
    }
  }

  def name(): String = synchronized {
    val providerID = s"${locale.name()}${seed.getOrElse("")}nameProvider"
    Faker.provide[String](providerID) {
      id =>
        NameProvider(locale)
          .setProviderID(id)
          .setLastName(true)
          .setFirstName(true)
          .setGender(Gender.All)
          .setSeed(seed)
    }
  }

  def nameMale(): String = synchronized {
    val providerID = s"${locale.name()}${seed.getOrElse("")}nameMaleProvider"
    Faker.provide[String](providerID) {
      id =>
        NameProvider(locale)
          .setProviderID(id)
          .setLastName(true)
          .setFirstName(true)
          .setGender(Gender.Male)
          .setSeed(seed)
    }
  }

  def nameFemale(): String = synchronized {
    val providerID = s"${locale.name()}${seed.getOrElse("")}nameFemaleProvider"
    Faker.provide[String](providerID) {
      id =>
        NameProvider(locale)
          .setProviderID(id)
          .setLastName(true)
          .setFirstName(true)
          .setGender(Gender.Female)
          .setSeed(seed)
    }
  }
}
