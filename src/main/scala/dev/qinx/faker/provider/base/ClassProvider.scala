package dev.qinx.faker.provider.base

import java.lang.annotation.Annotation
import java.lang.reflect.{Constructor, Parameter}

import dev.qinx.faker.internal._
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.provider.collection.ArrayProvider
import dev.qinx.faker.provider.data.SeriesProvider
import dev.qinx.faker.utils.{Constants, DefaultProvider, ReflectUtils}

import scala.annotation.tailrec
import scala.collection.mutable

class ClassProvider extends Provider[Object] with Logging with HasSeed {

  private[this] var cls: Option[Class[_]] = None
  private[this] val length: mutable.HashSet[Int] = new mutable.HashSet[Int]()
  private[this] lazy val declaredFields: Array[String] = this.cls.get.getDeclaredFields.map(_.getName)

  def setClass(cls: Class[_]): this.type = {
    this.cls = Option(cls)
    this
  }

  private[this] def paramNameOf(parameter: Parameter): String = {
    if (parameter.isNamePresent) {
      parameter.getName
    } else {
      declaredFields(parameter.getName.stripPrefix("arg").toInt)
    }
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
  private[this] lazy val primaryConstructorArgProviders: mutable.LinkedHashMap[String, CanProvide[_]] = {
    val providers = this.getProviders

    // handle series cross join
    providers.foreach { case (fromParam, provider) =>

      provider match {
        case sp: SeriesProvider =>
          sp.getCrossJoinTargetName match {
            case Some(toParam) =>
              val crossJoinTarget = providers.getOrElse(toParam,
                throw new NoSuchElementException(s"No such parameter: $toParam. If you are using scala 2.11, try to compile with javac option '-parameters'")
              )
              require(crossJoinTarget.isInstanceOf[SeriesProvider], "The cross join target field must also be a series")
              debug(s"Cross join $fromParam -> $toParam")
              sp.crossJoinWith(crossJoinTarget.asInstanceOf[SeriesProvider])
            case _ =>
          }
        case _ =>
      }
    }

    // update total length of if this class provider can provide a series
    providers.foreach { case (_, provider) =>
      provider match {
        case sp: SeriesProvider => if (!sp.hasCrossJoinTarget) this.length.add(sp.totalLength)
        case _ =>
      }
    }

    providers
  }

  def getDataSeriesLength: Int = {
    debug("Calculate data series length")

    this.length.size match {
      case 0 => 0
      case 1 => this.length.head
      case _ =>
        val arr = this.length.toArray.sorted
        leastCommonMultiplier(arr.head, arr.tail)
    }
  }

  @tailrec
  private[this] def leastCommonMultiplier(e: Int, arr: Array[Int]): Int = {
    if (arr.length == 1) {
      val high = Math.max(e, arr(0))
      val low = Math.min(e, arr(0))

      var lcm = high
      while (lcm % low != 0) {
        lcm += high
      }
      lcm
    } else {
      leastCommonMultiplier(arr.head, arr.tail)
    }
  }

  /**
   * For a given annotations, invoke the method "provider" and instantiate it.
   *
   * @param annotations annotations that has the provider method
   * @param param       parameter that has the annotations
   * @throws NoSuchMethodException cannot find the provider method in the annotations
   * @return an object of type CanProvide
   */
  @throws[NoSuchMethodException]
  private[this] def getProviderFromAnnotation(annotations: Array[Annotation], param: Parameter): CanProvide[_] = {
    val paramType = param.getType
    val paramName = paramNameOf(param)
    debug(s"Use provider defined in the annotation for the field <$paramName>: ${paramType.getCanonicalName}")

    var providerAnnotation: Option[Annotation] = None
    var componentAnnotation: Option[Annotation] = None

    annotations.length match {
      case 1 =>
        providerAnnotation = Some(annotations.head)

      case 2 =>

        annotations.foreach { a =>
          if (Constants.COLLECTION_PROVIDERS.contains(a.annotationType().getCanonicalName)) {
            providerAnnotation = Option(a)
          } else {
            componentAnnotation = Option(a)
          }
        }

        require(providerAnnotation.isDefined && componentAnnotation.isDefined,
          "When there are two provider annotations, one must be a collection provider " +
            "and the other one must be an element provider.")

      case _ => throw new IllegalArgumentException("Cannot handle more than two provider annotations")
    }

    val paramProvider = newInstanceOfProvider(providerAnnotation.get)

    paramProvider match {
      case provider: ArrayProvider =>
        debug("Find ArrayType provider")

        if (componentAnnotation.isDefined) {
          val componentProvider = newInstanceOfProvider(componentAnnotation.get)
          trace("Array provider will use the annotation configuration to set its component provider")
          provider.setComponentProvider(componentProvider)
        }
        provider.setComponentType(paramType.getComponentType)

      case provider: SeriesProvider =>
        debug("Find Series provider")

        if (!provider.hasData) {

          if (componentAnnotation.isDefined) {
            trace("A component provider is defined with an annotation. Series provider will use the annotation " +
              "configuration to set its component provider")
            val componentProvider = newInstanceOfProvider(componentAnnotation.get)
            provider.setComponentProvider(componentProvider)
          }

          provider.setComponentType(paramType)
        }

      case _ =>
    }

    paramProvider
  }

  private[this] def newInstanceOfProvider(annotation: Annotation): CanProvide[_] = {
    ReflectUtils
      .invokeAnnotationMethod[Class[CanProvide[_]]](annotation, "provider")
      .getDeclaredConstructor()
      .newInstance()
      .configure(annotation)
  }

  /**
   * If a seed is set in Faker, then try to set the seed for the given provider if it has seed.
   *
   * This method will not override any existing seed of the provider.
   *
   * @param provider provider that we want to set seed to
   */
  private[this] def setSeedOfProvider(provider: CanProvide[_]): Unit = {
    this.seed match {
      case Some(seed) =>
        // set seed only if the provider inherits the HasSeed trait
        if (classOf[HasSeed].isAssignableFrom(provider.getClass)) {
          trace(s"${provider.getClass.getCanonicalName} can have seed")
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
  private[this] def getProviders: mutable.LinkedHashMap[String, CanProvide[_]] = {
    this.seed match {
      case Some(seed) => info(s"Class provider seed was set to $seed")
      case _ =>
    }
    debug("Get constructor arg providers")

    val providers = new mutable.LinkedHashMap[String, CanProvide[_]]()

    primaryConstructor.getParameters.foreach { param =>
      val paramType = param.getType
      val paramName = paramNameOf(param)

      val annotation = param.getAnnotations.filter { anno =>
        ReflectUtils.hasDeclaredMethod(anno.annotationType(), "provider")
      }

      val provider = if (annotation.isEmpty) {
        debug(s"Use default provider for the field $paramName: ${paramType.getCanonicalName}")
        DefaultProvider.of(paramType)
      } else {
        getProviderFromAnnotation(annotation, param)
      }

      setSeedOfProvider(provider)
      providers.put(paramName, provider)
    }

    providers
  }

  /**
   * Generate the initial arguments for the primary constructor of the type T
   *
   * @return an array of Object
   */
  private[this] def getInitArgs: Array[Object] = {
    trace("Fake constructor arguments")
    primaryConstructor.getParameters.map { param =>
      val paramType = param.getType
      val paramName = paramNameOf(param)
      val provider = primaryConstructorArgProviders.getOrElse(paramName, throw new NoSuchElementException(s"Cannot find provider of type ${param.getType}"))

      trace(s"Fake data for parameter $paramName")

      val fakeData = provider.provide()
      val fakeDataCls: Class[_] = fakeData.getClass
      val paramCls: Class[_] = ReflectUtils.getClassOf(paramType)

      trace(s"param: $paramName, paramType: ${paramType.getCanonicalName}, fakeData class: ${fakeDataCls.getCanonicalName}")

      if (paramCls.isAssignableFrom(fakeDataCls)) {
        fakeData.asInstanceOf[Object]
      } else {
        ReflectUtils.provideArbitrary(paramType, provider) match {
          case Left(l) => l.asInstanceOf[Object]
          case Right(r) => r.asInstanceOf[Object]
        }
      }
    }
  }

  override def provide(): Object = primaryConstructor.newInstance(getInitArgs: _*).asInstanceOf[Object]

  override def configure(annotation: Annotation): this.type = this

}
