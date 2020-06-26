package dev.qinx.faker.provider.data

import java.lang.annotation.Annotation
import java.util.concurrent.atomic.AtomicInteger

import dev.qinx.faker.Faker
import dev.qinx.faker.internal.HasComponent
import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.ReflectUtils

import scala.collection.mutable

/**
 * A series provider will provide sequentially and iteratively each element of its inner list of data
 *
 * for example, with data Array(1, 2, 3), if repetition equals 1, then
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
class SeriesProvider extends Provider[Object] with HasComponent {

  /**
   * Times of repetition of each element
   */
  private[this] var rep: Int = 1
  private[this] val counter: AtomicInteger = new AtomicInteger(0)
  private[this] val index: AtomicInteger = new AtomicInteger(0)

  /**
   * length of data to be used in series data generation when only a component provider is given instead of data,
   * this value should only be set with the value of "length" in a @Series annotation
   */
  private[this] var _dataLength: Int = 0
  private[this] var data: Array[_] = Array()

  private[this] var _crossJoinTargetName: Option[String] = None
  private var _crossJoinTarget: Option[SeriesProvider] = None

  override def provide(): Object = {
    val output = data(index.get())
    counter.getAndIncrement()

    if (counter.compareAndSet(rep, 0)) {
      index.getAndIncrement()
      index.compareAndSet(data.length, 0)
    }
    output.asInstanceOf[Object]
  }

  override def setComponentType(componentType: Class[_]): SeriesProvider.this.type = {
    super.setComponentType(componentType)

    // initialize data
    require(this.provider.isDefined, "No component provider")
    val set: mutable.HashSet[Any] = mutable.HashSet[Any]()  // to remove duplicated data
    while (set.size < this._dataLength) {
      set.add(this.provider.get.provide())
    }

    this.setData(set.toArray)
    this
  }

  def hasData: Boolean = data.nonEmpty

  /**
   * Times of repetition of each element in data
   * @return
   */
  def repetition: Int = this.rep

  /**
   * Length of the inside data array
   * @return
   */
  def dataLength: Int = this.data.length

  /**
   * Total length of this series, which equals
   * {{{
   *   repetition * data_length
   * }}}
   * @return
   */
  def totalLength: Int = this.repetition * this.dataLength

  def updateRepetition(rep: Int): Unit = {
    this.rep = rep * this.rep

    if (hasCrossJoinTarget) {
      _crossJoinTarget.get.updateRepetition(rep)
    }
  }

  /**
   * Set the data of this series provider to the given array
   * @param data
   * @return
   */
  def setData(data: Array[_]): this.type = {
    debug("Set data to series provider")
    this.data = data.asInstanceOf[Array[Object]]
    this
  }

  def getCrossJoinTargetName: Option[String] = this._crossJoinTargetName

  /**
   * Return true if this series provider has a defined cross join target
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

    if (crossJoinTargetName != "") {
      this._crossJoinTargetName = Option(crossJoinTargetName)
    }

    if (seriesId != "") {
      this.setData(Faker.getSeries(seriesId))
    }

    this
  }

}
