package dev.qinx.faker.provider.person

import dev.qinx.faker.enums.{Gender, Locale}

class Person(val locale: Locale = Locale.en,
             val seed: Option[Long] = None) {

  private[this] val provider = NameProvider(locale).setSeed(seed)

  def lastName(): String = synchronized {
    provider.setLastName(true)
    provider.setFirstName(false)
    provider.setGender(Gender.All)
    provider.provide()
  }

  def firstName(): String = synchronized {
    provider.setLastName(false)
    provider.setFirstName(true)
    provider.setGender(Gender.All)
    provider.provide()
  }

  def firstNameMale(): String = synchronized {
    provider.setLastName(false)
    provider.setFirstName(true)
    provider.setGender(Gender.Male)
    provider.provide()
  }

  def firstNameFemale(): String = synchronized {
    provider.setLastName(false)
    provider.setFirstName(true)
    provider.setGender(Gender.Female)
    provider.provide()
  }

  def name(): String = synchronized {
    provider.setLastName(true)
    provider.setFirstName(true)
    provider.setGender(Gender.All)
    provider.provide()
  }

  def nameMale(): String = synchronized {
    provider.setLastName(true)
    provider.setFirstName(true)
    provider.setGender(Gender.Male)
    provider.provide()
  }

  def nameFemale(): String = synchronized {
    provider.setLastName(true)
    provider.setFirstName(true)
    provider.setGender(Gender.Female)
    provider.provide()
  }
}
