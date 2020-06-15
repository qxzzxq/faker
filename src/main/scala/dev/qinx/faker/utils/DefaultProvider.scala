package dev.qinx.faker.utils

import java.time.LocalDate

import dev.qinx.faker.internal.CanProvide
import dev.qinx.faker.provider.LocalDateProvider

object DefaultProvider {

  def of(obj: Any): CanProvide = {
    obj match {
      case ld: LocalDate => new LocalDateProvider
    }
  }

}
