package dev.qinx.faker.provider.data

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.collection.ArrayType
import dev.qinx.faker.annotation.data.Gaussian
import org.scalatest.funsuite.AnyFunSuite

class GaussianProviderSuite extends AnyFunSuite {

  import GaussianProviderSuite._

  test("Gaussian provider should provide a normal distribution") {

    val faker = new Faker[Test0]
    val data = faker.get(1000)

    assert(data.map(_.g0).sum / 1000 > -1)
    assert(data.map(_.g0).sum / 1000 < 1)

    assert(data.map(_.g1.toDouble).sum / 1000 > 99)
    assert(data.map(_.g1.toDouble).sum / 1000 < 101)

    assert(data.map(_.g2.get).sum / 1000 > -1)
    assert(data.map(_.g2.get).sum / 1000 < 1)

    val faker2 = new Faker[Test1]
    assert((faker2.get().g0.sum / 1000) > 9)
    assert((faker2.get().g0.sum / 1000) < 11)

  }


}

object GaussianProviderSuite {

  case class Test0(@Gaussian g0: Double,
                   @Gaussian(std = 1, mean = 100) g1: String,
                   @Gaussian g2: Option[Double])

  case class Test1(@ArrayType(length = 1000) @Gaussian(mean = 10) g0: Array[Double])

}
