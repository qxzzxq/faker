package dev.qinx.faker.provider.person

import java.lang.annotation.Annotation

import dev.qinx.faker.enums.{Gender, Locale}
import dev.qinx.faker.internal.HasRandom
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.{Constants, ReflectUtils}

private[faker] abstract class LocalNameProvider(val locale: Locale) extends Provider[String] with HasRandom {

  protected var _firstName: Boolean = true
  protected var _lastName: Boolean = true
  protected var _gender: Gender = Gender.All

  def setFirstName(boolean: Boolean): this.type = {
    _firstName = boolean
    this
  }

  def setLastName(boolean: Boolean): this.type = {
    _lastName = boolean
    this
  }

  def setGender(gender: Gender): this.type = {
    this._gender = gender
    this
  }

  protected val firstNamesMale: Array[String]

  protected val lastNames: Array[String]

  protected val firstNamesFemale: Array[String]

  protected lazy val firstNames: Array[String] = firstNamesFemale ++ firstNamesMale

  protected def firstNameOf(names: Array[String]): Option[String] = {
    if (_firstName) {
      randomElementFrom(names)
    } else {
      None
    }
  }

  protected def lastName: Option[String] = {
    if (_lastName) {
      randomElementFrom(lastNames)
    } else {
      None
    }
  }

  protected def randomElementFrom(arr: Array[String]): Option[String] = {
    arr.lift(random.nextInt(arr.length))
  }

  protected def getMaleName: String = {
    Array(firstNameOf(firstNamesMale), lastName).flatten.mkString(" ")
  }

  protected def getFemaleName: String = {
    Array(firstNameOf(firstNamesFemale), lastName).flatten.mkString(" ")
  }

  protected def getName: String = {
    Array(firstNameOf(firstNames), lastName).flatten.mkString(" ")
  }

  override def provide(): String = {
    _gender match {
      case Gender.All => getName
      case Gender.Male => getMaleName
      case Gender.Female => getFemaleName
      case _ => throw new IllegalArgumentException("Not supported gender")
    }
  }

  override def configure(annotation: Annotation): LocalNameProvider.this.type = {
    this.setFirstName(ReflectUtils.invokeAnnotationMethod[String](annotation, "firstName").toBoolean)
    this.setLastName(ReflectUtils.invokeAnnotationMethod[String](annotation, "lastName").toBoolean)
    this.setGender(ReflectUtils.invokeAnnotationMethod[Gender](annotation, "gender"))
    this.setSeed(annotation)
    this
  }

  protected def defaultResourcePath(name: String): String = s"${Constants.RESOURCE_DATA}/person/${locale.name()}/$name"
}
