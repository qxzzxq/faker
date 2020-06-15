package dev.qinx.faker.utils

import java.time.LocalDate

import dev.qinx.faker.internal.CanProvide
import dev.qinx.faker.provider.{LocalDateProvider, StringProvider}

object DefaultProvider {

  def of(obj: Class[_]): CanProvide = {
    if (obj.equals(classOf[LocalDate])) {
      return new LocalDateProvider()
    }

    if (obj.equals(classOf[String])) {
      return new StringProvider()
    }


    null
  }

}
