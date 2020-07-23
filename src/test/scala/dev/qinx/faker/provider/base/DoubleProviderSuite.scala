package dev.qinx.faker.provider.base

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.DoubleType
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class DoubleProviderSuite extends AnyFunSuite {

  import DoubleProviderSuite._

  test("FloatProvider should provide float") {

    val faker = new Faker[TestDoubleProvider]
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())
    println(faker.get())

    (1 to 100) foreach { _ =>
      val c2 = faker.get().c2
      assert(c2 >= 100 && c2 <= 101)
    }
  }

  test("FloatProvider exception") {
    val faker = new Faker[TestDoubleProviderException]
    assertThrows[IllegalArgumentException](faker.get())

  }

  test("Set seed") {
    new SeedTester[TestDoubleProvider]
  }

}

object DoubleProviderSuite {

  case class TestDoubleProvider(@DoubleType c1: String,
                                @DoubleType(min = 100, max = 101) c2: Double,
                                @DoubleType c3: Option[Double])

  case class TestDoubleProviderException(@DoubleType c1: String,
                                         @DoubleType(min = 0, max = 1) c2: Int)

}



