package dev.qinx.faker.internal

import org.apache.logging.log4j.{LogManager, Logger}

/**
 * Logging provide logging features for the class that extends this trait
 */
private[faker] trait Logging {

  // Make the log field transient so that objects with Logging can
  // be serialized and used on another machine
  @transient private var logger: Logger = _

  // Method to get or create the logger for this object
  protected def log: Logger = {
    if (logger == null) {
      logger = LogManager.getLogger(logName)
    }
    logger
  }

  // Method to get the logger name for this object
  protected def logName: String = {
    // Ignore trailing $'s in the class names for Scala objects
    this.getClass.getName.stripSuffix("$")
  }

  def debug(message: String): Unit = {
    if (log.isDebugEnabled()) {
      log.debug(message)
    }
  }

  def trace(message: String): Unit = {
    if (log.isTraceEnabled()) {
      log.trace(message)
    }
  }

  def info(message: String): Unit = {
    if (log.isInfoEnabled()) {
      log.info(message)
    }
  }

  def warn(message: String): Unit = {
    if (log.isWarnEnabled()) {
      log.warn(message)
    }
  }

  def error(message: String): Unit = {
    if (log.isErrorEnabled()) {
      log.error(message)
    }
  }

  def fatal(message: String): Unit = {
    if (log.isFatalEnabled()) {
      log.fatal(message)
    }
  }

}
