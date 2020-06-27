package dev.qinx.faker.internal

import java.lang.annotation.Annotation

import dev.qinx.faker.utils.ReflectUtils


trait HasSeed {

   var seed: Option[Long] = None

  def hasSeed: Boolean = this.seed.nonEmpty

  def setSeed(seed: Long): this.type = {
    this.setSeed(Some(seed))
  }

  def setSeed(seed: Option[Long]): this.type = {
    if (seed != this.seed) {
      this.seed = seed
    }
    this
  }

  protected def getSeedFromAnnotation(annotation: Annotation): Option[Long] = {
    val seed = ReflectUtils.invokeAnnotationMethod[String](annotation, "seed")
    seed match {
      case "" => None
      case _ =>
        try {
          Some(seed.toLong)
        } catch {
          case _: NumberFormatException => throw new NumberFormatException("Cannot cast the seed to long")
        }
    }
  }

  protected def setSeed(annotation: Annotation): Unit = {
    val s = getSeedFromAnnotation(annotation)
    if (s.isDefined) {
      this.setSeed(s)
    }
  }

}
