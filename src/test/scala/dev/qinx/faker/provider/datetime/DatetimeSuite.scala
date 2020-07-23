package dev.qinx.faker.provider.datetime

import dev.qinx.faker.Faker
import org.scalatest.funsuite.AnyFunSuite

class DatetimeSuite extends AnyFunSuite {

  test("Classic faker API") {

    assert(!Faker.hasProvider("LocalDateProvider"))
    println(Faker.datetime().localDate())
    assert(Faker.hasProvider("LocalDateProvider"))
    println(Faker.datetime().localDate())
    println(Faker.datetime().localDateTime())
    println(Faker.datetime().localTime())

  }

}
