package dev.qinx.faker.provider.data

import java.lang.annotation.Annotation
import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import dev.qinx.faker.Faker
import dev.qinx.faker.internal.{HasComponent, HasOption, HasSeed, HasString}
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

import scala.collection.mutable

/**
 * A series provider will provide sequentially and iteratively each element of its inner list of data.
 *
 * For example, with data {{{Array(1, 2, 3)}}}
 *
 * if repetition equals 1, then
 * {{{
 *   seriesProvider.provide()  // 1
 *   seriesProvider.provide()  // 2
 *   seriesProvider.provide()  // 3
 *   seriesProvider.provide()  // 1
 *   seriesProvider.provide()  // 2
 *   ...
 * }}}
 *
 * If repetition = 2, then
 * {{{
 *   seriesProvider.provide()  // 1
 *   seriesProvider.provide()  // 1
 *   seriesProvider.provide()  // 2
 *   seriesProvider.provide()  // 2
 *   seriesProvider.provide()  // 3
 *   ...
 * }}}
 */
class SeriesProvider
  extends Provider[Object]
    with HasComponent
    with HasString
    with HasOption[Object]
    with HasSeed {

  /**
   * Times of repetition of each element
   */
  private[this] var rep: Int = 1
  private[this] val counter: AtomicInteger = new AtomicInteger(0)
  private[this] val index: AtomicInteger = new AtomicInteger(0)

  private[this] var _string: Option[String] = None
  private[this] var _option: Option[Option[Object]] = None
  /**
   * length of data to be used in series data generation when only a component provider is given instead of data,
   * this value should only be set with the value of "length" in a @Series annotation
   */
  private[this] var _dataLength: Int = 0
  private[this] var data: Array[_] = Array()

  private[this] var _crossJoinTargetName: Option[String] = None
  private var _crossJoinTarget: Option[SeriesProvider] = None

  private[this] val componentProviderSeedUpdated: AtomicBoolean = new AtomicBoolean(false)


  override def provide(): Object = {
    val output = data(index.get())

    this._string = Option(output.toString)
    this._option = Option(Option(output.asInstanceOf[Object]))

    counter.getAndIncrement()

    if (counter.compareAndSet(rep, 0)) {
      index.getAndIncrement()
      index.compareAndSet(data.length, 0)
    }
    output.asInstanceOf[Object]
  }

  override def setComponentType(componentType: Class[_]): SeriesProvider.this.type = {
    super.setComponentType(componentType)

    //TODO fix faker.setSeed()
    this.updateSeedOfComponentProvider()

    // initialize data
    require(this.componentProvider.isDefined, "No component provider")
    val set: mutable.LinkedHashSet[Any] = mutable.LinkedHashSet[Any]() // to remove duplicated data
    while (set.size < this._dataLength) {
      set.add(this.componentProvider.get.provide())
    }

    this.setData(set.toArray)
    this
  }

  def hasData: Boolean = data.nonEmpty

  /**
   * Times of repetition of each element in data
   *
   * @return
   */
  def repetition: Int = this.rep

  /**
   * Length of the inside data array
   *
   * @return
   */
  def dataLength: Int = this.data.length

  /**
   * Total length of this series, which equals
   * {{{
   *   repetition * data_length
   * }}}
   *
   * @return
   */
  def totalLength: Int = this.repetition * this.dataLength

  /**
   * Update the repetition of this series provider and its cross join target's (if exists) repetition.
   *
   * The new repetition will be {{{old_rep * new_rep}}}
   * @param rep new repetition to be set
   */
  def updateRepetition(rep: Int): Unit = {
    trace(s"Update repetition from ${this.rep} to ${rep * this.rep}")
    this.rep = rep * this.rep

    if (hasCrossJoinTarget) {
      _crossJoinTarget.get.updateRepetition(rep)
    }
  }

  /**
   * Set the data of this series provider to the given array
   *
   * @param data data to be used in the series provider
   * @return
   */
  def setData(data: Array[_]): this.type = {
    this.data = data.asInstanceOf[Array[Object]]
    this
  }

  def getCrossJoinTargetName: Option[String] = this._crossJoinTargetName

  /**
   * Return true if this series provider has a defined cross join target
   *
   * @return
   */
  def hasCrossJoinTarget: Boolean = {
    _crossJoinTarget.isDefined
  }

  def crossJoinWith(seriesProvider: SeriesProvider): this.type = {
    require(data.nonEmpty, "Series provider data must be set")
    this._crossJoinTarget = Option(seriesProvider)
    seriesProvider.updateRepetition(this.totalLength)
    this
  }

  override def configure(annotation: Annotation): SeriesProvider.this.type = {
    val seriesId = ReflectUtils.invokeAnnotationMethod[String](annotation, "id")
    val crossJoinTargetName = ReflectUtils.invokeAnnotationMethod[String](annotation, "crossJoin")
    this._dataLength = ReflectUtils.invokeAnnotationMethod[Int](annotation, "length")
    val s = getSeedFromAnnotation(annotation)
    if (s.isDefined) {
      this.setSeed(s)
    }

    if (crossJoinTargetName != "") {
      debug(s"Set cross join target name: $crossJoinTargetName")
      this._crossJoinTargetName = Option(crossJoinTargetName)
    }

    if (seriesId != "") {
      debug(s"Set series id: $seriesId")
      this.setData(Faker.getSeries(seriesId))
    }

    this
  }

  override def provideString: String = {
    this._string match {
      case Some(s) => s
      case _ => throw new NoSuchElementException("The method provide() should be invoked before provideString()")
    }
  }

  override def provideOption: Option[Object] = this._option match {
    case Some(o) => o
    case _ => throw new NoSuchElementException("The method provide() should be invoked before provideOption()")
  }

  /**
   * Check if this array provider has seed. If true then try to update the seed of the component provider.
   * But if the component provider already has a seed, then we skip the updating
   */
  private[this] def updateSeedOfComponentProvider(): Unit = {
    require(componentProvider.isDefined, "No component provider is set")
    trace("Update seed of component provider")

    if (classOf[HasSeed].isAssignableFrom(this.componentProvider.get.getClass) && !componentProviderSeedUpdated.getAndSet(true)) {
      val p = this.componentProvider.get.asInstanceOf[HasSeed]

      if (this.hasSeed && !p.hasSeed) {
        debug("Override component provider seed")
        p.setSeed(this.seed)
      } else {
        debug("Component provider already has a seed, do not override")
      }
    }
  }
}
