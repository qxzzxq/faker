package dev.qinx.faker.provider.fallback

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}
import dev.qinx.faker.provider.Provider

import scala.collection.immutable.Stream

class StringProvider extends Provider[String] with HasRandom with Logging {

  private[this] def alphanumeric: Stream[Char] = {
    def nextAlphaNum: Char = {
      val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
      chars charAt (random nextInt chars.length)
    }

    Stream continually nextAlphaNum
  }

  override def provide(): String = alphanumeric.take(10).mkString

  override def configure(annotation: Annotation): this.type = this
}
