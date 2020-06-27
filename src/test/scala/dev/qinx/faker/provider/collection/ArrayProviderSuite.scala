package dev.qinx.faker.provider.collection

import java.time.LocalDate

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.{DoubleType, IntType}
import dev.qinx.faker.annotation.collection.ArrayType
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

  test("Multiple annotation") {

    val faker = new Faker[TestArrayProvider3]

    faker.get(100).foreach { data =>
      assert(data.arr.length === 10)
      data.arr.foreach(i => assert(i >= 0 && i <= 10))
    }
    assertThrows[IllegalArgumentException](new Faker[TestArrayProvider4].get())
  }

  test("ArrayProvider should handle set seed") {
    val faker1 = new Faker[TestArrayProvider3]().setSeed(100)
    val faker2 = new Faker[TestArrayProvider3]().setSeed(100)

    assert(faker1.get().arr.mkString(", ") === faker2.get().arr.mkString(", "))
    println("---------------")

    val faker3Bis1 = new Faker[TestArrayProvider3bis]()
    val faker3Bis2 = new Faker[TestArrayProvider3bis]()
    val data3bis1 = faker3Bis1.get()
    val data3bis2 = faker3Bis2.get()
    assert(data3bis1.arr.mkString(", ") === data3bis2.arr.mkString(", "))

    println("---------------")
    val faker3ter = new Faker[TestArrayProvider3ter]
    val data3ter = faker3ter.get()
    assert(data3ter.arr.mkString(", ") !== data3bis2.arr.mkString(", "))

    val faker3 = new Faker[TestArrayProvider3]().setSeed(10)
    val data3 = faker3.get()
    assert(data3.arr.mkString(", ") === data3bis2.arr.mkString(", "))
  }

}

object ArrayProviderSuite {

  case class TestArrayProvider(c1: String, c2: Array[LocalDate])
  case class TestArrayProvider2(c1: String, @ArrayType(length = 5) c2: Array[LocalDate])

  case class TestArrayProvider3(@ArrayType(length = 10) @IntType(min = 0, max = 10) arr: Array[Int])

  case class TestArrayProvider3bis(@ArrayType(length = 10, seed = "10") @IntType(min = 0, max = 10) arr: Array[Int])

  case class TestArrayProvider3ter(@ArrayType(length = 10, seed = "10") @IntType(min = 0, max = 10, seed = "11") arr: Array[Int])

  case class TestArrayProvider4(@DoubleType @IntType(min = 0, max = 10) arr: Array[Int])
}
