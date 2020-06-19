package dev.qinx.faker.provider.datetime

import java.time.LocalDateTime

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.datetime.DateTime
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class LocalDateTimeProviderSuite extends AnyFunSuite {

  import LocalDateTimeProviderSuite._

  test("TestLocalTimeProvider should provide local datetime") {

    val faker = new Faker[TestLocalDateTimeProvider]
    println(faker.get())
    assert(faker.get().time3 === "10:00:00")

  }

  test("Test seed") {
    new SeedTester[TestLocalDateTimeProvider]
  }

}


object LocalDateTimeProviderSuite {

  case class TestLocalDateTimeProvider(@DateTime time: String,
                                       @DateTime time2: LocalDateTime,
                                       @DateTime(format = "10:00:00") time3: String)

}
