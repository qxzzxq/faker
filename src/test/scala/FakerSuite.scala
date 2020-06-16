import FakerSuite.{TestClass, TestName}
import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.datetime.Date
import dev.qinx.faker.annotation.person.Name
import dev.qinx.faker.enums.Local
import org.scalatest.funsuite.AnyFunSuite

class FakerSuite extends AnyFunSuite {

  test("Faker should generate fake data") {
    val faker = new Faker[TestClass]
    println(faker.get())
  }

  test("name") {
    val faker = new Faker[TestName]
    faker.get(10).foreach(println)
  }

  test("Method") {

    println(Faker.name())
    println(Faker.localDate())
  }
}

object FakerSuite {

  case class TestName(@Name(local = Local.zh_CN) chineseName: String,
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
