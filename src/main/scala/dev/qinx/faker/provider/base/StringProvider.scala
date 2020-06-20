package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

import scala.collection.immutable.Stream

class StringProvider extends Provider[String] with HasRandom with Logging {

  import StringProvider._

  private[this] var pattern: String = "## ??"
  private[this] var letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
  private[this] var digits = "0123456789"

  def setLetters(letters: String): this.type = {
    this.letters = letters
    this
  }

  def setDigits(digits: String): this.type = {
    this.digits = digits
    this
  }

  def setPattern(pattern: String): this.type = {
    this.pattern = pattern
    this
  }

  private[this] def next(collection: String): Char = {
    if (collection.length > 0) {
      collection charAt (random nextInt collection.length)
    } else {
      0.toChar
    }
  }

  private[this] def nextLetter: Char = next(letters)

  private[this] def nextDigit: Char = next(digits)

  private[this] def placeholderToChar(placeholder: Char): Char = {
    placeholder match {
      case NUMBER => nextDigit
      case LETTER => nextLetter
      case _ => placeholder
    }
  }

  private[this] def nextAlphaNum: Char = {
    letters charAt (random nextInt letters.length)
  }

  override def provide(): String = pattern.map(placeholderToChar)

  @throws[NumberFormatException]
  override def configure(annotation: Annotation): this.type = {
    this.setPattern(ReflectUtils.invokeAnnotationMethod[String](annotation, "pattern"))
    this.setDigits(ReflectUtils.invokeAnnotationMethod[String](annotation, "digits"))
    this.setLetters(ReflectUtils.invokeAnnotationMethod[String](annotation, "letters"))
    this.setSeed(annotation)
    this
  }
}

object StringProvider {

  val NUMBER: Char = '#'
  val LETTER: Char = '?'

}
