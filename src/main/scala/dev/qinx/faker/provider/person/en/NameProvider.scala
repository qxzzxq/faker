package dev.qinx.faker.provider.person.en

import dev.qinx.faker.internal.HasResource
import dev.qinx.faker.provider.person.LocalNameProvider
import dev.qinx.faker.utils.Constants

class NameProvider extends LocalNameProvider with HasResource {

  override val firstNamesMale: Array[String] = {
    val data = allLinesOf(s"${Constants.RESOURCE_DATA}/person/en/first_names_male.txt")
    data.flatMap(l => l.split(","))
  }

  override val firstNamesFemale: Array[String] = {
    val data = allLinesOf(s"${Constants.RESOURCE_DATA}/person/en/first_names_female.txt")
    data.flatMap(l => l.split(","))
  }

  override val lastNames: Array[String] ={
    val data = allLinesOf(s"${Constants.RESOURCE_DATA}/person/en/last_names.txt")
    data.flatMap(l => l.split(","))
  }

}

