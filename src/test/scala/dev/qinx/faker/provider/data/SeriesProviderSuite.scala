package dev.qinx.faker.provider.data

import java.time.LocalDate

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.IntType
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


}

object SeriesProviderSuite {

  case class Test0(c1: String)

  case class Test1(@Series(length = 3) c1: String)

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

}
