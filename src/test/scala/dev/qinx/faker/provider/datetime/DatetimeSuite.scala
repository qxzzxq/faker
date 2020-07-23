package dev.qinx.faker.provider.datetime

import dev.qinx.faker.Faker
import org.scalatest.funsuite.AnyFunSuite

class DatetimeSuite extends AnyFunSuite {

  test("Classic faker API") {
    assert(Faker.datetime().localDate() != null)
    assert(Faker.datetime().localDateTime() != null)
    assert(Faker.datetime().localTime() != null)
    println(Faker.datetime().localDate())
    println(Faker.datetime().localDate())
    println(Faker.datetime().localDateTime())
    println(Faker.datetime().localTime())
  }

}
