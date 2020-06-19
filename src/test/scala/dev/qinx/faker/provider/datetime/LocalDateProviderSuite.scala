package dev.qinx.faker.provider.datetime

import java.time.LocalDate

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.datetime.Date
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class LocalDateProviderSuite extends AnyFunSuite {
  import LocalDateProviderSuite._

  test("LocalDateProvider should provide local date") {

    val faker = new Faker[TestLocalDateProvider]
    println(faker.get())
    assert(faker.get().date3 === "2020-01-01")

  }

  test("Test seed") {
    new SeedTester[TestLocalDateProvider]
  }

}

object LocalDateProviderSuite {

  case class TestLocalDateProvider(@Date date: String,
                                   @Date date2: LocalDate,
                                   @Date(format = "2020-01-01") date3: String)

}

