package dev.qinx.faker.provider.datetime

import java.time.LocalTime

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.datetime.Time
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class LocalTimeProviderSuite extends AnyFunSuite {
  import LocalTimeProviderSuite._

  test("LocalTimeProvider should provide local time") {

    val faker = new Faker[TestLocalTimeProvider]
    println(faker.get())
    assert(faker.get().time3 === "10:00:00")

  }

  test("Test seed") {
    new SeedTester[TestLocalTimeProvider]
  }

}

object LocalTimeProviderSuite {

  case class TestLocalTimeProvider(@Time time: String,
                                   @Time time2: LocalTime,
                                   @Time(format = "10:00:00") time3: String)

}

