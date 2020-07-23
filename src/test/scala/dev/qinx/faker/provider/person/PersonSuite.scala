package dev.qinx.faker.provider.person

import dev.qinx.faker.Faker
import dev.qinx.faker.enums.Locale
import org.scalatest.funsuite.AnyFunSuite

class PersonSuite extends AnyFunSuite {

  test("Classic faker API") {
    println(Faker.person().firstName())
    println(Faker.person().lastName())
    println(Faker.person().firstNameFemale())
    println(Faker.person().name())
    println(Faker.person().nameFemale())
    println(Faker.person().nameFemale())
    println(Faker.person().nameFemale())
    println(Faker.person().nameFemale())
    println(Faker.person(seed = Some(100)).nameFemale())
  }

  test("Locale name") {
    println(Faker.person(Locale.zh_CN).firstName())
    println(Faker.person(Locale.zh_CN).lastName())
    println(Faker.person(Locale.zh_CN).firstNameFemale())
    println(Faker.person(Locale.zh_CN).name())
    println(Faker.person(Locale.zh_CN).nameFemale())
    println(Faker.person(Locale.zh_CN).nameFemale())
    println(Faker.person(Locale.zh_CN).nameFemale())
    println(Faker.person(Locale.zh_CN).nameFemale())
    println(Faker.person(Locale.zh_CN).nameFemale())
  }

}
