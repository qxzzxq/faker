package dev.qinx.faker.internal

trait HasSeed {

  protected var seed: Option[Long] = None

  def setSeed(seed: Long): this.type = {
    this.seed = Some(seed)
    this
  }

}
