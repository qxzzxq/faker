package dev.qinx.faker.utils

import dev.qinx.faker.Faker

import scala.reflect.ClassTag


class SeedTester[T : ClassTag](times: Int = 100) {

  val faker: Faker[T] = new Faker[T].setSeed(100)
  val faker2: Faker[T] = new Faker[T].setSeed(100)
  val faker3: Faker[T] = new Faker[T]

  (1 to times) foreach { _ =>
    val fd = faker.get()
    val fd2 = faker2.get()
    val fd3 = faker3.get()
    assert(fd.equals(fd2))
    assert(!fd.equals(fd3))
  }

}
