package dev.qinx.faker.provider.person

import java.lang.annotation.Annotation

import dev.qinx.faker.enums.Gender
import dev.qinx.faker.internal.HasRandom
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

private[person] abstract class LocalNameProvider extends Provider[String] with HasRandom {

  protected var firstName: Boolean = true
  protected var lastName: Boolean = true
  protected var gender: Gender = Gender.All

  protected val firstNamesMale: Array[String]

  protected val lastNames: Array[String]

  protected val firstNamesFemale: Array[String]

  protected lazy val firstNames: Array[String] = firstNamesFemale ++ firstNamesMale

  private[this] def firstNameOf(names: Array[String]): Option[String] = {
    if (firstName) {
      randomElementFrom(names)
    } else {
      None
    }
  }

  private[this] def lastNameOf: Option[String] = {
    if (lastName) {
      randomElementFrom(lastNames)
    } else {
      None
    }
  }

  protected def randomElementFrom(arr: Array[String]): Option[String] = {
    arr.lift(random.nextInt(arr.length))
  }

  protected def getMaleName: String = {
    Array(firstNameOf(firstNamesMale), lastNameOf).flatten.mkString(" ")
  }

  protected def getFemaleName: String = {
    Array(firstNameOf(firstNamesFemale), lastNameOf).flatten.mkString(" ")
  }

  protected def getName: String = {
    Array(firstNameOf(firstNames), lastNameOf).flatten.mkString(" ")
  }

  override def provide(): String = {
    this.gender match {
      case Gender.All => getName
      case Gender.Male => getMaleName
      case Gender.Female => getFemaleName
      case _ => throw new IllegalArgumentException("Not supported gender")
    }
  }

  override def configure(annotation: Annotation): LocalNameProvider.this.type = {
    this.firstName = ReflectUtils.invokeAnnotationMethod[String](annotation, "firstName").toBoolean
    this.lastName = ReflectUtils.invokeAnnotationMethod[String](annotation, "lastName").toBoolean
    this.gender = ReflectUtils.invokeAnnotationMethod[Gender](annotation, "gender")
    this
  }
}
