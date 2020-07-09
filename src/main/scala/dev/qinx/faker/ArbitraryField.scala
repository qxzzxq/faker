package dev.qinx.faker

class ArbitraryField(val name: String) {

  private[faker] var value: Any => Any = _

  def <<(value: Any => Any): this.type = {
    this.value = value
    this
  }

}
