import FakerSuite.TestClass
import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.Date
import org.scalatest.funsuite.AnyFunSuite

class FakerSuite extends AnyFunSuite {

  test("Faker should generate fake data") {

    val faker = new Faker[TestClass]

    println(faker.get())

  }



}

object FakerSuite {

  case class TestClass(@Date date: String,
                       text: String,
                       float: Float,
                       double: Double,
                       long: Long,
                       int: Int,
                       short: Short,
                       char: Char,
                       bool: Boolean,
                       bytes: Array[Byte]) {


  }
}
