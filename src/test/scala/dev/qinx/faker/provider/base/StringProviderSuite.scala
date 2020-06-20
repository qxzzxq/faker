package dev.qinx.faker.provider.base

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.Text
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class StringProviderSuite extends AnyFunSuite {

  import StringProviderSuite._

  test("StringProvider should provide string") {
    val faker = new Faker[StringProviderSuiteTestClass]()

    (1 to 10) foreach { _ =>
      val data = faker.get()
      val data2 = faker.get()
      assert(data.id !== data2.id)
      assert(data.code === "123" && data.col3 === "11-aaa")
    }
  }

  test("String provider seed") {
    val tester = new SeedTester[SeedOverride]()
    tester.test { f =>
      val data = f.get()
      data.col1 === data.col2 && data.col3 === data.col4 && data.col1 != data.col3
    }
  }

}

object StringProviderSuite {

  case class StringProviderSuiteTestClass(@Text
                                          id: String,
                                          @Text(pattern = "123", letters = "", seed = "1")
                                          code: String,
                                          @Text(pattern = "##-???", letters = "a", digits = "1")
                                          col3: String)

  case class SeedOverride(@Text col1: String,
                          @Text col2: String,
                          @Text(seed = "111") col3: String,
                          @Text(seed = "111") col4: String)

}
