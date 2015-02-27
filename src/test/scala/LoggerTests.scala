package journal

import org.slf4j.helpers.NOPLogger
import org.scalacheck.Properties

object LoggerTests extends Properties("Logger") {
  private[this] val log: Logger = Logger(NOPLogger.NOP_LOGGER)
  private[this] val up: Throwable = new RuntimeException("blargh")

  property("doesn't eagerly evaluate") = {
    var evaluated: Boolean = false
    def evaluateMessage(): String = {
      evaluated = true
      "a crazy side-effecting string"
    }

    // without cause
    log.debug(evaluateMessage())
    log.info(evaluateMessage())
    log.warn(evaluateMessage())
    log.error(evaluateMessage())

    // with cause
    log.debug(evaluateMessage(), up)
    log.info(evaluateMessage(), up)
    log.warn(evaluateMessage(), up)
    log.error(evaluateMessage(), up)

    !evaluated
  }

}
