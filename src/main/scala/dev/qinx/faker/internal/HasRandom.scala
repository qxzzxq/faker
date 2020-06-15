package dev.qinx.faker.internal

import scala.util.Random

trait HasRandom extends HasSeed {

  protected lazy val random: Random = {
    val r = Random
    seed match {
      case Some(s) =>
        r.setSeed(s)
      case _ =>
    }
    r
  }

}
