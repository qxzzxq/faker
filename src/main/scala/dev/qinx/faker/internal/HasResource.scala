package dev.qinx.faker.internal

import java.nio.file.{Files, Paths}

trait HasResource {

  def allLinesOf(name: String): Array[String] = {
    import collection.JavaConverters._
    val inputURI = this.getClass.getClassLoader.getResource(name).toURI
    val inputPath = Paths.get(inputURI)
    Files.readAllLines(inputPath).asScala.toArray
  }

}
