package dev.qinx.faker.provider.person.en

import dev.qinx.faker.enums.Locale
import dev.qinx.faker.internal.HasResource
import dev.qinx.faker.provider.person.LocalNameProvider

class NameProvider extends LocalNameProvider(Locale.en) with HasResource {

  override val firstNamesMale: Array[String] = allLinesOf(defaultResourcePath("first_names_male.txt")).flatMap(l => l.split(","))

  override val firstNamesFemale: Array[String] = allLinesOf(defaultResourcePath("first_names_female.txt")).flatMap(l => l.split(","))

  override val lastNames: Array[String] = allLinesOf(defaultResourcePath("last_names.txt")).flatMap(l => l.split(","))

}

