package dev.qinx.faker.provider.base

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.LongType
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class LongProviderSuite extends AnyFunSuite {

  import LongProviderSuite._

  test("IntProvider should provide Integer") {

    val faker = new Faker[TestLongProvider]

    (1 to 100) foreach { _ =>
      assert(faker.get().c2 >= 0 && faker.get().c2 <= 1)
    }

  }

  test("Test seed") {
    new SeedTester[TestLongProviderSeed]
  }

}

object LongProviderSuite {

  case class TestLongProvider(@LongType c1: String,
                              @LongType(min = 0, max = 1) c2: Long)

  case class TestLongProviderSeed(@LongType c1: String,
                                  @LongType c2: Long)

}