package dev.qinx.faker

import dev.qinx.faker.FakerSuite.{CompleteTestClass, MyProvider, TestClass, TestName}
import dev.qinx.faker.annotation.base.{DoubleType, LongType}
import dev.qinx.faker.annotation.datetime.Date
import dev.qinx.faker.annotation.geo.{Lat, Lon}
import dev.qinx.faker.annotation.person.Name
import dev.qinx.faker.enums.Locale
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.provider.base.IntProvider
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class FakerSuite extends AnyFunSuite {

  test("Faker should generate fake data") {
    val faker = new Faker[TestClass]
    val data = faker.get()

    assert(data.date != null)
    assert(data.text != null)
    assert(!data.float.isNaN)
    assert(!data.double.isNaN)
    assert(!data.long.isNaN)
    assert(!data.int.isNaN)
    assert(!data.short.isNaN)
    assert(data.char.isValidByte)
    assert(data.bool || !data.bool)
    assert(!data.bytes.isEmpty)

    faker.get(10) foreach println
  }

  test("Faker should generate name") {
    val faker = new Faker[TestName]
    faker.get(10).foreach { data =>
      println(data)
      assert(data.date != null)
      assert(data.text != null)
      assert(!data.number.isNaN)
      assert(data.chineseName.nonEmpty)
      assert(data.englishName.nonEmpty)
    }
  }

  test("We should be able to call the companions methods") {
    println(Faker.person(Locale.zh_CN).name())
    assert(Faker.person().name().nonEmpty)
    assert(Faker.localDate() != null)
  }

  test("Faker should generate same data for the same seed") {
    new SeedTester[TestName]
  }

  test("Complete test") {
    val faker = new Faker[CompleteTestClass]()
    val data = faker.get()
    println(data)
    assert(data.date != null)
    assert(!data.lon.isNaN)
    assert(!data.lat.isNaN)
    assert(data.name.nonEmpty)
    assert(data.name2.nonEmpty)
    assert(data.numeric.nonEmpty)
    assert(!data.numeric2.isNaN)
    assert(data.numeric3.isDefined)

    new SeedTester[CompleteTestClass]()
  }

  test("Faker singleton") {

    Faker.array[TestName](1).foreach(println)
    println(Faker.person().name())
    println(Faker.localDate())

    Faker.array[String](5).foreach(println)

    val provider = new IntProvider
    println(Faker.provide[Int](provider))

    assert(Faker.provide[String](new MyProvider) === "haha")
    assert(Faker.provide[String](new MyProvider) === "haha")

    assert(Faker.transport().airport().length === 3)
  }

}

object FakerSuite {

  class MyProvider extends Provider[String] {
    println("instantiate my provider")
    override def provide(): String = "haha"
  }

  case class TestName(@Name(locale = Locale.zh_CN) chineseName: String,
                      @Name englishName: String,
                      @Date date: String,
                      text: String,
                      number: Int)

  case class TestClass(@Date date: String,
                       text: String,
                       float: Float,
                       double: Double,
                       long: Long,
                       int: Int,
                       short: Short,
                       char: Char,
                       bool: Boolean,
                       bytes: Array[Byte])

  case class CompleteTestClass(@Date date: String,
                               @Lon lon: Double,
                               @Lat lat: Double,
                               @Name name: String,
                               @Name(locale = Locale.zh_CN) name2: String,
                               @DoubleType numeric: String,
                               @DoubleType numeric2: Double,
                               @LongType numeric3: Option[Long])

}
