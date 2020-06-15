package dev.qinx.faker.provider

import java.lang.annotation.Annotation

import dev.qinx.faker.internal.{HasRandom, Logging}

class IntProvider extends Provider[Int] with HasRandom with Logging  {

  override def provide(): Int = random.nextInt()

  override def configure(annotation: Annotation): IntProvider.this.type = this

}
