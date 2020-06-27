package dev.qinx.faker.provider.company

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.company.Company
import dev.qinx.faker.enums.Locale
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class CompanyProviderSuite extends AnyFunSuite {
  import CompanyProviderSuite._

  test("Company provider should provide by default a real company name") {
    val faker = new Faker[Test1]
    faker.get(100).foreach {d =>
      assert(d !== null)
    }
  }

  test("Company provider code") {
    val faker = new Faker[Test2]
    faker.get(1000).foreach {d =>
      assert(d.name.length <= 6)
    }
  }

  test("Company provider provide false name") {
    val faker = new Faker[Test3]
    faker.get(100).foreach {d =>
      println(d)
      assert(d.name.nonEmpty)
    }
  }

  test("Company provider locale") {
    val faker = new Faker[Test4]
    faker.get(100).foreach {d =>
      println(d)
      assert(d.name.nonEmpty)
    }
  }

  test("Handle seed") {
    new SeedTester[Test1]()
    new SeedTester[Test2]()
    new SeedTester[Test3](50)
  }



}

object CompanyProviderSuite {
  case class Test1(@Company name: String)
  case class Test2(@Company(code = true) name: String)
  case class Test3(@Company(real = false) name: String)
  case class Test4(@Company(locale = Locale.zh_CN, real = false) name: String)
}
