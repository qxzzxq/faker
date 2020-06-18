package dev.qinx.faker.utils

import dev.qinx.faker.internal.HasResource

import scala.reflect.ClassTag

private[faker] class CSVReader[T: ClassTag](val filePath: String,
                                            val header: Boolean = false,
                                            val separator: String = ",",
                                            val quote: String = "\"") extends HasResource {

  private[this] val cls = implicitly[ClassTag[T]].runtimeClass
  private[this] val constructor = try {
    cls.getConstructor(classOf[Array[String]])
  } catch {
    case _: NoSuchMethodException =>
      throw new NoSuchMethodException(s"The class ${cls.getSimpleName} shoud have the following constructor: " +
        s"def this(data: Array[String])")
  }

  assert(separator.length == 1, "Cannot accept separators with more than 1 char")
  assert(quote.length == 1, "Cannot accept quote marks with more than 1 char")

  def rawData: Array[String] = allLinesOf(filePath)

  def loadData: Array[T] = {
    val data = if (header) {
      rawData.tail
    } else {
      rawData
    }

    data.map { r =>
      val args = split(r).map(removeQuote andThen unescape)
      constructor.newInstance(args).asInstanceOf[T]
    }
  }

  /**
   * Unescape quote marks inside a column
   */
  private[this] val unescape: String => String = s => {
    quote match {
      case "\"" => s.replace(s"$quote$quote", s"$quote")
      case _ => s
    }
  }

  /**
   * Remove starting and trailing quote mark
   */
  private[this] val removeQuote: String => String = s => {
    s.stripPrefix(quote).stripSuffix(quote)
  }

  /**
   * Split the string by the separator
   */
  private[this] val split: String => Array[String] = s => {
    var insideQuote = false
    val wordBuffer = collection.mutable.ListBuffer[Char]()
    val output = collection.mutable.ListBuffer[String]()

    s.foreach {
      c =>
        if (c.equals(quote.charAt(0))) insideQuote = !insideQuote
        if (c.equals(separator.charAt(0)) && !insideQuote) {
          output += wordBuffer.mkString
          wordBuffer.clear()
        } else {
          wordBuffer += c
        }
    }

    output += wordBuffer.mkString

    output.toArray
  }
}
