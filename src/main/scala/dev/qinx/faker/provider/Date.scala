package dev.qinx.faker.provider

import java.time.LocalDate

class Date extends Provider[LocalDate] {

  private[this] var _seed: Long = 0L

  override def seed(seed: Long): Date.this.type = {
    _seed = seed
    this
  }

  override def provide(): LocalDate = LocalDate.now()

}
