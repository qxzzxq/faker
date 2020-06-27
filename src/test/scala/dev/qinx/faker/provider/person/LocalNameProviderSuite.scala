package dev.qinx.faker.provider.person

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.person.Name
import dev.qinx.faker.enums.{Gender, Locale}
import dev.qinx.faker.provider.person.LocalNameProviderSuite.{MyLocalNameProvider, TestName1, TestName2}
import dev.qinx.faker.utils.SeedTester
import org.scalatest.funsuite.AnyFunSuite

class LocalNameProviderSuite extends AnyFunSuite {

  test("Local name provider should provide name") {
    val localNameProvider = new MyLocalNameProvider()
    val name = localNameProvider.provide()
    assert(name === "male1 ln1" || name === "male2 ln1" || name === "female1 ln1" || name === "female2 ln1")
  }

  test("Local name provider set gender") {
    val localNameProvider = new MyLocalNameProvider().setGender(Gender.Female)
    (0 to 100).foreach { _ =>
      val name = localNameProvider.provide()
      assert(name === "female1 ln1" || name === "female2 ln1")
    }

    val maleLocalNameProvider = new MyLocalNameProvider().setGender(Gender.Male)
    (0 to 100).foreach { _ =>
      val name = maleLocalNameProvider.provide()
      assert(name === "male1 ln1" || name === "male2 ln1")
    }
  }

  test("Set first name and last name") {
    val firstNameProvider = new MyLocalNameProvider().setGender(Gender.Female).setLastName(false)
    (0 to 100).foreach { _ =>
      val name = firstNameProvider.provide()
      assert(name === "female1" || name === "female2")
    }

    val lastNameProvider = new MyLocalNameProvider().setFirstName(false)
    (0 to 100).foreach { _ =>
      val name = lastNameProvider.provide()
      assert(name === "ln1")
    }
  }

  test("resource path") {
    val p = new MyLocalNameProvider
    assert(p.path === "data/person/en/test.txt")
  }

  test("Handle seed") {
    new SeedTester[TestName1]()

    val faker2a = new Faker[TestName2]
    val faker2b = new Faker[TestName2]

    assert(faker2a.get() === faker2b.get())
  }

}

object LocalNameProviderSuite {

  case class TestName1(@Name c1: String)
  case class TestName2(@Name(seed = "10") c1: String)

  class MyLocalNameProvider extends LocalNameProvider(locale = Locale.en) {
    override protected val firstNamesMale: Array[String] = Array("male1", "male2")
    override protected val lastNames: Array[String] = Array("ln1")
    override protected val firstNamesFemale: Array[String] = Array("female1", "female2")

    def path: String = this.defaultResourcePath("test.txt")
  }

}
