package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation
import java.lang.reflect.Constructor

import dev.qinx.faker.internal._
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.{DefaultProvider, ReflectUtils}

import scala.collection.mutable

class ClassProvider extends Provider[Object] with Logging with HasSeed {

  private[this] var cls: Option[Class[_]] = None

  def setClass(cls: Class[_]): this.type = {
    this.cls = Option(cls)
    this
  }

  /**
   * The primary constructor of the class T
   */
  private[this] lazy val primaryConstructor: Constructor[_] = {
    if (cls.isEmpty) {
      throw new NoSuchElementException("No class has been set yet")
    }

    val constructor = cls.get
      .getDeclaredConstructors
      .head

    constructor.setAccessible(true)
    constructor.asInstanceOf[Constructor[_]]
  }

  /**
   * The list of provider for each parameter of the primary constructor.
   */
  private[this] lazy val providers: mutable.LinkedHashMap[String, CanProvide] = this.getProviders

  /**
   * For a given annotation,
   *
   * @param annotation annotation that has the provider method
   * @throws NoSuchMethodException cannot find the provider method in the annotation
   * @return an object of type CanProvide
   */
  @throws[NoSuchMethodException]
  private[this] def getProviderFromAnnotation(annotation: Annotation): CanProvide = {
    val provider = ReflectUtils
      .invokeAnnotationMethod[Class[CanProvide]](annotation, "provider")
      .getDeclaredConstructor()
      .newInstance()
      .configure(annotation)
    provider
  }

  /**
   * If a seed is set in Faker, then try to set the seed for the given provider if it has seed
   *
   * @param provider provider that we want to set seed to
   */
  private[this] def setSeedOfProvider(provider: CanProvide): Unit = {
    this.seed match {
      case Some(seed) =>
        // set seed only if the provider inherits the HasSeed trait
        if (classOf[HasSeed].isAssignableFrom(provider.getClass)) {
          if (log.isTraceEnabled()) {
            log.trace(s"${provider.getClass.getCanonicalName} can have seed")
          }

          // Do not override the existing seed
          if (!provider.asInstanceOf[HasSeed].hasSeed) {
            provider.asInstanceOf[HasSeed].setSeed(seed)
          }
        }
      case _ =>
    }
  }

  /**
   * Get the data providers for each parameter of the primary constructor
   *
   * @return a Map of parameter name to its data provider
   */
  @throws[NoSuchElementException]("No provider could be found")
  @throws[NoSuchMethodException]
  private[this] def getProviders: mutable.LinkedHashMap[String, CanProvide] = {

    this.seed match {
      case Some(seed) => log.info(s"Set seed to $seed")
      case _ =>
    }

    val providers = new mutable.LinkedHashMap[String, CanProvide]()

    primaryConstructor.getParameters.foreach { p =>
      val annotation = p.getAnnotations.find { a =>
        ReflectUtils.hasDeclaredMethod(a.annotationType(), "provider")
      }

      val provider = annotation match {
        case Some(anno) =>
          log.debug(s"Set ${anno.annotationType().getSimpleName} provider for the field ${p.getName}: ${p.getType.getCanonicalName}")
          getProviderFromAnnotation(anno)
        case _ =>
          log.debug(s"Set default provider for the field ${p.getName}: ${p.getType.getCanonicalName}")
          DefaultProvider.of(p.getType)
      }

      setSeedOfProvider(provider)
      providers.put(p.getName, provider)
    }

    providers
  }

  /**
   * Generate the initial arguments for the primary constructor of the type T
   *
   * @return an array of Object
   */
  private[this] def getInitArgs: Array[Object] = {
    primaryConstructor.getParameters.map { param =>
      val paramType = param.getType
      val provider = providers.getOrElse(param.getName, throw new NoSuchElementException(s"Cannot find provider of type ${param.getType}"))

      if (log.isTraceEnabled()) {
        log.trace(s"Generate fake data for ${param.getName}")
      }

      val fakeData = provider.provide()
      val fakeDataCls: Class[_] = fakeData.getClass
      val paramCls: Class[_] = ReflectUtils.getClassOf(paramType)

      if (log.isTraceEnabled()) {
        log.trace(s"param: ${param.getName}, paramType: ${paramType.getCanonicalName}, fakeData class: ${fakeDataCls.getCanonicalName}")
      }

      if (paramCls.isAssignableFrom(fakeDataCls)) {
        fakeData.asInstanceOf[Object]
      } else {

        if (paramType.equals(classOf[String]) && classOf[HasString].isAssignableFrom(provider.getClass)) {
          // handle the string conversion
          provider.asInstanceOf[HasString].provideString.asInstanceOf[Object]

        } else if (paramType.equals(classOf[Option[_]]) && classOf[HasOption[_]].isAssignableFrom(provider.getClass)) {
          // handle the Option conversion
          provider.asInstanceOf[HasOption[_]].provideOption.asInstanceOf[Object]

        } else {
          throw new NoSuchElementException(s"Cannot provide ${fakeDataCls.getSimpleName} to a field of type $paramType")
        }
      }

    }
  }

  override def provide(): Object = primaryConstructor.newInstance(getInitArgs: _*).asInstanceOf[Object]

  override def configure(annotation: Annotation): this.type = this

}
