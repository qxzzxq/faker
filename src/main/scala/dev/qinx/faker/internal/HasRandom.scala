package dev.qinx.faker.internal

import java.util.Random

trait HasRandom extends HasSeed {

  protected lazy val random: Random = {
    val r = new Random()
    seed match {
      case Some(s) => r.setSeed(s)
      case _ =>
    }
    r
  }

}
