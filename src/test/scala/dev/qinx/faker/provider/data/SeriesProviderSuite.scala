package dev.qinx.faker.provider.data

import java.time.LocalDate

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.{FloatType, IntType, Text}
import dev.qinx.faker.annotation.data.Series
import dev.qinx.faker.annotation.datetime.Date
import dev.qinx.faker.annotation.person.Name
import org.scalatest.funsuite.AnyFunSuite

class SeriesProviderSuite extends AnyFunSuite {
  import SeriesProviderSuite._

  test("Series provider should provide a finite length seq") {
    val faker0 = new Faker[Test0]
    assert(faker0.getDataSeries.length === 0)

    val faker = new Faker[Test1]
    val data = faker.getDataSeries

    assert(data.length === 3)
    assert(data.map(_.c1).distinct.length === 3)

    val data6 = faker.get(6)
    assert(data6.map(_.c1).distinct.length === 3)

    assert(faker.getDataSeries.sameElements(faker.getDataSeries))
  }

  test("Series provider should handle multiple annotations") {
    val faker = new Faker[Test2]
    assert(faker.getDataSeries.length === 40)

    val faker3 = new Faker[Test3]
    assert(faker3.getDataSeries.length === 10)
  }

  test("Series provider should handle cross join") {
    val faker4 = new Faker[Test4]
    assert(faker4.getDataSeries.length === 25)
    faker4.getDataSeries.foreach(println)

    val lists = faker4.getDataSeries.groupBy(_.c1).map { case (key, list) =>
      list.map(_.c2.toString).sorted.mkString(",")
    }

    assert(lists.toSet.size === 1)
  }

  test("Series provider should handle id") {
    val faker5 = new Faker[Test5]
    faker5.putSeries("A", Array(1L, 2L, 3L))
    assert(faker5.getDataSeries.length === 15)
  }

  test("Series provider should calculate correctly data length") {
    val faker6 = new Faker[Test6]
    faker6.putSeries("A", Array(1L, 2L, 3L))
    assert(faker6.getDataSeries.length === 30)
  }

  test("Series provider should handle cross join with different order") {
    val faker8 = new Faker[Test8]
    faker8.putSeries("myInput", Array("apple", "banana", "orange"))

    faker8.getDataSeries.foreach(println)
    println("******************")

    val faker7 = new Faker[Test7]
    faker7.putSeries("myInput", Array("apple", "banana", "orange"))

    faker7.getDataSeries.foreach(println)
  }

  test("Series provider should handle seed") {
//    val tester = new SeedTester[Test1]()
//    new SeedTester[Test2]()
//    new SeedTester[Test3]()
//    new SeedTester[Test4]()
//    new SeedTester[Test5]()
//    new SeedTester[Test6]()
//    new SeedTester[Test7]()
//    new SeedTester[Test8]()

    val faker1 = new Faker[Test10]
    val faker2 = new Faker[Test10]
    assert(faker1.getDataSeries.sameElements(faker2.getDataSeries))
    println("----------")
    val faker11A = new Faker[Test11]
    val faker11B = new Faker[Test11]
    println(faker11A.get)
    println(faker11B.get)
    assert(faker11A.getDataSeries.sameElements(faker11B.getDataSeries))

  }


}

object SeriesProviderSuite {

  case class Test0(c1: String)

  case class Test1(@Series(length = 3) c1: String)
  case class Test10(@Series(length = 3, seed = "10") c1: String)
  case class Test11(@Series(length = 3, seed = "10") @Name c1: String)
  case class Test12(@Series(length = 3, seed = "10") @Name(seed = "11") c1: String)

  case class Test2(@Series(length = 5) @Name c1: String,
                   @Series(length = 8) @IntType c2: String)

  case class Test3(@Series(length = 5) @Name c1: String,
                   @Series(length = 10) @IntType c2: Option[Int])

  case class Test4(@Series(length = 5) @Name c1: String,
                   @Series(length = 5, crossJoin = "c1") @Date c2: Option[LocalDate])

  case class Test5(@Series(length = 5) @Name c1: String,
                   @Series(id = "A", crossJoin = "c1") c2: Long)

  case class Test6(@Series(length = 5) @Name c1: String,
                   @Series(id = "A", crossJoin = "c1") c2: Long,
                   @Series(length = 10) c3: Int)

  case class Test7(@Series(length = 2) @Date date: String,
                   @Series(length = 3, crossJoin = "date") @Text(pattern = "??-###") id: String,
                   @Series(id = "myInput", crossJoin = "id") name: String,
                   @FloatType(min = 10, max = 20) price: Float)

  case class Test8(@Series(length = 2) @Date date: String,
                   @Series(length = 3, crossJoin = "name") @Text(pattern = "??-###") id: String,
                   @Series(id = "myInput", crossJoin = "date") name: String,
                   @FloatType(min = 10, max = 20) price: Float)
}
