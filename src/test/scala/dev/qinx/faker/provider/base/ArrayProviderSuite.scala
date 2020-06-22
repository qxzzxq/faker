package dev.qinx.faker.provider.base

import java.time.LocalDate

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.ArrayType
import org.scalatest.funsuite.AnyFunSuite

class ArrayProviderSuite extends AnyFunSuite {

  import ArrayProviderSuite._

  test("Array provider should provide array") {
    val faker = new Faker[TestArrayProvider]()

    println(faker.get())
    faker.get().c2.foreach(println)
    assert(faker.get().c2.length === 3)

    val faker2 = new Faker[TestArrayProvider2]
    println(faker2.get())
    faker2.get().c2.foreach(println)
    assert(faker2.get().c2.length === 5)
  }

  test("Set seed") {
    val faker = new Faker[TestArrayProvider]().setSeed(100)
    val faker2 = new Faker[TestArrayProvider]().setSeed(100)

    val fd = faker.get()
    val fd2 = faker2.get()

    assert(fd.c1 === fd2.c1)
    assert(fd.c2.sameElements(fd2.c2))
  }

}

object ArrayProviderSuite {

  case class TestArrayProvider(c1: String, c2: Array[LocalDate])
  case class TestArrayProvider2(c1: String, @ArrayType(length = 5) c2: Array[LocalDate])

}
