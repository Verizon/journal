//: ----------------------------------------------------------------------------
//: Copyright (C) 2015 Verizon.  All Rights Reserved.
//:
//:   Licensed under the Apache License, Version 2.0 (the "License");
//:   you may not use this file except in compliance with the License.
//:   You may obtain a copy of the License at
//:
//:       http://www.apache.org/licenses/LICENSE-2.0
//:
//:   Unless required by applicable law or agreed to in writing, software
//:   distributed under the License is distributed on an "AS IS" BASIS,
//:   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//:   See the License for the specific language governing permissions and
//:   limitations under the License.
//:
//: ----------------------------------------------------------------------------

package journal

import org.slf4j.helpers.NOPLogger
import org.slf4j.{Marker, Logger => Backend}
import org.scalacheck.Properties

object LoggerTests extends Properties("Logger") {
  private[this] val up: Throwable = new RuntimeException("blargh")

  property("doesn't eagerly evaluate") = {
    val log: Logger = Logger(NOPLogger.NOP_LOGGER)

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

  property("does evaluate when required") = {
    object SilentLogger extends Backend {
      //  So...many...methods...
      override def warn(msg: String): Unit = ()
      override def warn(format: String, arg: scala.Any): Unit = ()
      override def warn(format: String, arguments: AnyRef*): Unit = ()
      override def warn(format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def warn(msg: String, t: Throwable): Unit = ()
      override def warn(marker: Marker, msg: String): Unit = ()
      override def warn(marker: Marker, format: String, arg: scala.Any): Unit = ()
      override def warn(marker: Marker, format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def warn(marker: Marker, format: String, arguments: AnyRef*): Unit = ()
      override def warn(marker: Marker, msg: String, t: Throwable): Unit = ()
      override def isErrorEnabled: Boolean = true
      override def isErrorEnabled(marker: Marker): Boolean = true
      override def getName: String = "test"
      override def isInfoEnabled: Boolean = true
      override def isInfoEnabled(marker: Marker): Boolean = true
      override def isDebugEnabled: Boolean = true
      override def isDebugEnabled(marker: Marker): Boolean = ???
      override def isTraceEnabled: Boolean = true
      override def isTraceEnabled(marker: Marker): Boolean = true
      override def error(msg: String): Unit = ()
      override def error(format: String, arg: scala.Any): Unit = ()
      override def error(format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def error(format: String, arguments: AnyRef*): Unit = ()
      override def error(msg: String, t: Throwable): Unit = ()
      override def error(marker: Marker, msg: String): Unit = ()
      override def error(marker: Marker, format: String, arg: scala.Any): Unit = ()
      override def error(marker: Marker, format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def error(marker: Marker, format: String, arguments: AnyRef*): Unit = ()
      override def error(marker: Marker, msg: String, t: Throwable): Unit = ()
      override def debug(msg: String): Unit = ()
      override def debug(format: String, arg: scala.Any): Unit = ()
      override def debug(format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def debug(format: String, arguments: AnyRef*): Unit = ()
      override def debug(msg: String, t: Throwable): Unit = ()
      override def debug(marker: Marker, msg: String): Unit = ()
      override def debug(marker: Marker, format: String, arg: scala.Any): Unit = ()
      override def debug(marker: Marker, format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def debug(marker: Marker, format: String, arguments: AnyRef*): Unit = ()
      override def debug(marker: Marker, msg: String, t: Throwable): Unit = ()
      override def isWarnEnabled: Boolean = true
      override def isWarnEnabled(marker: Marker): Boolean = true
      override def trace(msg: String): Unit = ()
      override def trace(format: String, arg: scala.Any): Unit = ()
      override def trace(format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def trace(format: String, arguments: AnyRef*): Unit = ()
      override def trace(msg: String, t: Throwable): Unit = ()
      override def trace(marker: Marker, msg: String): Unit = ()
      override def trace(marker: Marker, format: String, arg: scala.Any): Unit = ()
      override def trace(marker: Marker, format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def trace(marker: Marker, format: String, argArray: AnyRef*): Unit = ()
      override def trace(marker: Marker, msg: String, t: Throwable): Unit = ()
      override def info(msg: String): Unit = ()
      override def info(format: String, arg: scala.Any): Unit = ()
      override def info(format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def info(format: String, arguments: AnyRef*): Unit = ()
      override def info(msg: String, t: Throwable): Unit = ()
      override def info(marker: Marker, msg: String): Unit = ()
      override def info(marker: Marker, format: String, arg: scala.Any): Unit = ()
      override def info(marker: Marker, format: String, arg1: scala.Any, arg2: scala.Any): Unit = ()
      override def info(marker: Marker, format: String, arguments: AnyRef*): Unit = ()
      override def info(marker: Marker, msg: String, t: Throwable): Unit = ()
    }

    val log: Logger = Logger(SilentLogger)

    var evaluationCount: Int = 0
    def evaluateMessage(): String = {
      evaluationCount += 1
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

    evaluationCount == 8
  }
}
