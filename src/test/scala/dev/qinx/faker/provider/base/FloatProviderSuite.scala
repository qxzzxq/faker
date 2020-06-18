package dev.qinx.faker.provider.base

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.FloatType
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class FloatProviderSuite extends AnyFunSuite {

  import FloatProviderSuite._

  test("FloatProvider should provide float") {

    val faker = new Faker[TestFloatProvider]
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())

    (1 to 100) foreach { _ =>
      val c2: Float = faker.get().c2
      assert(c2 >= 100 && c2 <= 101)
    }
  }

  test("FloatProvider exception") {
    val faker = new Faker[TestFloatProviderException]
    assertThrows[NoSuchElementException](faker.get())

  }

  test("Set seed") {
    new SeedTester[TestFloatProvider]
  }

}

object FloatProviderSuite {

  case class TestFloatProvider(@FloatType c1: String,
                               @FloatType(min = 100, max = 101) c2: Float,
                               @FloatType c3: Option[Float])

  case class TestFloatProviderException(@FloatType c1: String,
                                        @FloatType(min = 0, max = 1) c2: Int)

}
