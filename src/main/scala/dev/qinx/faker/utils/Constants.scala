package dev.qinx.faker.utils

import dev.qinx.faker.annotation.collection.ArrayType
import dev.qinx.faker.annotation.data.Series

private[faker] object Constants {
  val PACKAGE: String = "dev.qinx.faker"
  val PROVIDER_PACKAGE: String = s"$PACKAGE.provider"
  val PERSON_PROVIDER_PACKAGE: String = s"$PROVIDER_PACKAGE.person"
  val GEO_PROVIDER_PACKAGE: String = s"$PROVIDER_PACKAGE.geo"

  val RESOURCE_DATA: String = "data"

  val COLLECTION_PROVIDERS: Array[String] = Array(
    classOf[ArrayType].getCanonicalName,
    classOf[Series].getCanonicalName
  )
}
