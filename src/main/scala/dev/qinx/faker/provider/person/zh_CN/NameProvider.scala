package dev.qinx.faker.provider.person.zh_CN

import dev.qinx.faker.internal.HasResource
import dev.qinx.faker.provider.person.LocalNameProvider
import dev.qinx.faker.utils.Constants

class NameProvider extends LocalNameProvider with HasResource {

  override val firstNamesMale: Array[String] = {
    val data = allLinesOf(s"${Constants.RESOURCE_DATA}/person/zh_CN/first_names_male.txt")
    data.flatMap(l => l.split(","))
  }

  override val firstNamesFemale: Array[String] = {
    val data = allLinesOf(s"${Constants.RESOURCE_DATA}/person/zh_CN/first_names_female.txt")
    data.flatMap(l => l.split(","))
  }

  override val lastNames: Array[String] ={
    val data = allLinesOf(s"${Constants.RESOURCE_DATA}/person/zh_CN/last_names.txt")
    data.flatMap(l => l.split(","))
  }

  override protected def getMaleName: String = {
    Array(lastNameOf, firstNameOf(firstNamesMale)).flatten.mkString("")
  }

  override protected def getFemaleName: String = {
    Array(lastNameOf, firstNameOf(firstNamesFemale)).flatten.mkString("")
  }

  override protected def getName: String = {
    Array(lastNameOf, firstNameOf(firstNames)).flatten.mkString("")
  }
}
