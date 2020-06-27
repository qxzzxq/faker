package dev.qinx.faker.provider.company

import java.lang.annotation.Annotation

import dev.qinx.faker.entity.Company
import dev.qinx.faker.enums.Locale
import dev.qinx.faker.internal.{HasRandom, HasResource, Logging}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.provider.person.NameProvider
import dev.qinx.faker.utils.{CSVReader, Constants, ReflectUtils}

class CompanyProvider extends Provider[String] with HasResource with HasRandom with Logging {

  private[this] var code: Boolean = false
  private[this] var realCompany: Boolean = true
  private[this] var locale: Locale = Locale.en
  private[this] lazy val realCompanies: Array[Company] = new CSVReader[Company](s"${Constants.RESOURCE_DATA}/company/companies.csv").loadData
  private[this] lazy val lastNameProvider = NameProvider(locale).setFirstName(false).setSeed(seed)
  private[this] lazy val companySuffixes = CompanyProvider.companySuffixesOf(locale)

  def generateCompanyCode(boolean: Boolean): this.type = {
    this.code = boolean
    this
  }

  def realCompany(boolean: Boolean): this.type = {
    this.realCompany = boolean
    this
  }

  def setLocale(locale: Locale): this.type = {
    info("Locale support is still a WIP.")
    this.locale = locale
    this
  }

  private[this] def randomSuffix: String = {
    companySuffixes(this.random.nextInt(companySuffixes.length))
  }
  override def provide(): String = {
    val company = if (realCompany) {
      realCompanies(this.random.nextInt(realCompanies.length))
    } else {
      val name = lastNameProvider.provide()
      Company(name.slice(0, 3).toUpperCase(), s"$name$randomSuffix", "")
    }
    if (code) {
      company.code
    } else {
      company.name
    }
  }

  override def configure(annotation: Annotation): CompanyProvider.this.type = {
    this.generateCompanyCode(ReflectUtils.invokeAnnotationMethod[Boolean](annotation, "code"))
    this.realCompany(ReflectUtils.invokeAnnotationMethod[Boolean](annotation, "real"))
    this.setLocale(ReflectUtils.invokeAnnotationMethod[Locale](annotation, "locale"))
    this.setSeed(getSeedFromAnnotation(annotation))
    this
  }
}

object CompanyProvider {
  private[company] val companySuffixesOf: Map[Locale, Array[String]] = Map(
    Locale.en -> Array(" Inc", " and Sons", " LLC", " Group", " PLC", " Ltd"),
    Locale.zh_CN -> Array("氏兄弟", "有限公司", "集团")
  ).withDefaultValue(Array(" Inc", " and Sons", " LLC", " Group", " PLC", " Ltd"))
}