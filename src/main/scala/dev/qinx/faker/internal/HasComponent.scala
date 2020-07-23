package dev.qinx.faker.internal

import dev.qinx.faker.provider.Provider
import dev.qinx.faker.utils.DefaultProvider

trait HasComponent extends Logging {

  var componentProvider: Option[Provider[_]] = None
  var componentType: Option[Class[_]] = None

  /**
   * Set the provider of the component of the array
   * @param provider a provider that can provide the array component
   * @return
   */
  def setComponentProvider(provider: Provider[_]): this.type = {
    debug(s"Set component provider ${provider.getClass.getCanonicalName}")
    this.componentProvider = Option(provider)
    this
  }

  /**
   * Set the component type of this array provider. If no provider was set before calling this method, it will
   * set the default provider. Otherwise, it won't override the existing provider
   * @param componentType class of the component
   * @return
   */
  def setComponentType(componentType: Class[_]): this.type = {
    debug(s"Set component type to ${componentType.getCanonicalName}")
    this.componentType = Option(componentType)
    if (componentProvider.isEmpty) {
      debug("Use default provider as the component provider")
      this.setComponentProvider(DefaultProvider.of(componentType))
    }
    this
  }

}
