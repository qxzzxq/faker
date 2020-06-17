package dev.qinx.faker

import dev.qinx.faker.FakerSuite.{TestClass, TestName}
import dev.qinx.faker.annotation.base.IntegerType
import dev.qinx.faker.annotation.datetime.Date
import dev.qinx.faker.annotation.person.Name
import dev.qinx.faker.enums.Locale
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
    assert(Faker.name().nonEmpty)
    assert(Faker.localDate() != null)
  }

  test("Faker should generate same data for the same seed") {
    val faker = new Faker[TestName].setSeed(100)
    val faker2 = new Faker[TestName].setSeed(100)

    assert(faker.get === faker2.get)
    assert(faker.get === faker2.get)
    assert(faker.get === faker2.get)
    assert(faker.get === faker2.get)
    assert(faker.get === faker2.get)
  }

}

object FakerSuite {

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
}
