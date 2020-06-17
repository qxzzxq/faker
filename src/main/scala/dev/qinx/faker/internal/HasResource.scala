package dev.qinx.faker.internal

import java.io.{BufferedReader, InputStreamReader}
import java.util.stream.Collectors

trait HasResource {

  def allLinesOf(name: String): Array[String] = {
    import collection.JavaConverters._
    val inputStream = this.getClass.getClassLoader.getResourceAsStream(name)
    val lines = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.toList())
    lines.asScala.toArray
  }

}
