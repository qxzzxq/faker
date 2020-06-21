package dev.qinx.faker.provider.base

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.IntType
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class IntProviderSuite extends AnyFunSuite {

  import IntProviderSuite._
  test("IntProvider should provide Integer") {

    val faker = new Faker[TestIntProvider]

    (1 to 100) foreach { _ =>
      assert(Array(0, 1).contains(faker.get().c2))
    }

  }

  test("Test seed") {
    new SeedTester[TestIntProviderSeed]
  }

}

object IntProviderSuite {

  case class TestIntProvider(@IntType c1: String,
                             @IntType(min = 0, max = 1) c2: Int)

  case class TestIntProviderSeed(@IntType c1: String,
                                 @IntType c2: Int)

}
