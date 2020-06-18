package dev.qinx.faker.internal

trait HasSeed {

  protected var seed: Option[Long] = None

  def setSeed(seed: Long): this.type = {
    this.seed = Some(seed)
    this
  }

  def setSeed(seed: Option[Long]): this.type = {
    this.seed = seed
    this
  }

}
