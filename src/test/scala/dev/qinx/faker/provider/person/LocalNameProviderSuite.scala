package dev.qinx.faker.provider.person

import dev.qinx.faker.enums.{Gender, Locale}
import dev.qinx.faker.provider.person.LocalNameProviderSuite.MyLocalNameProvider
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

}

object LocalNameProviderSuite {

  class MyLocalNameProvider extends LocalNameProvider(locale = Locale.en) {
    override protected val firstNamesMale: Array[String] = Array("male1", "male2")
    override protected val lastNames: Array[String] = Array("ln1")
    override protected val firstNamesFemale: Array[String] = Array("female1", "female2")

    def path: String = this.defaultResourcePath("test.txt")
  }

}
