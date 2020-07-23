package dev.qinx.faker.provider.transport

import dev.qinx.faker.Faker
import org.scalatest.funsuite.AnyFunSuite

class TransportSuite extends AnyFunSuite {

  test("Classic Faker API") {
    println(Faker.transport().airport())
    println(Faker.transport().airport())
    println(Faker.transport().airport())
    println(Faker.transport().airport())
  }

  test("With filter country") {
    (1 to 100).foreach(_ => println(Faker.transport().airport(Option("FR"))))
  }

}
