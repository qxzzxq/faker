package dev.qinx.faker.provider.base

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.IntType
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class IntProviderSuite extends AnyFunSuite {

  import IntProviderSuite._
  test("IntProvider should provide Integer") {

    val faker = new Faker[TestIntProvider]
    println(faker.get())
    assert(faker.get().c2 === 0)

  }

  test("Test seed") {
    new SeedTester[TestIntProvider]
  }

}

object IntProviderSuite {

  case class TestIntProvider(@IntType c1: String,
                             @IntType(min = 0, max = 1) c2: Int)

}
