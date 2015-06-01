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

import org.slf4j.{Logger => Backend, LoggerFactory}
import scalaz.concurrent._
import scala.reflect.macros.Context
import java.util.concurrent.{ThreadFactory, Executors}

sealed class Logger(val backend: Backend, val handler: Actor[LogMessage]) {
  def error(message: String): Unit =
    macro LoggerMacro.errorMessage
  def error(message: String, cause: Throwable): Unit =
    macro LoggerMacro.errorMessageCause

  def warn(message: String): Unit =
    macro LoggerMacro.warnMessage
  def warn(message: String, cause: Throwable): Unit =
    macro LoggerMacro.warnMessageCause

  def info(message: String): Unit =
    macro LoggerMacro.infoMessage
  def info(message: String, cause: Throwable): Unit =
    macro LoggerMacro.infoMessageCause

  def debug(message: String): Unit =
    macro LoggerMacro.debugMessage
  def debug(message: String, cause: Throwable): Unit =
    macro LoggerMacro.debugMessageCause
}

/**
 * Borrowed generously and adapted from scala-logging (https://github.com/typesafehub/scala-logging)
 * which is under the Apache 2.0 license.
 *
 * Thanks to Heiko Seeberger et al for creating Scala Logging.
 */
private object LoggerMacro {
  type LoggerContext = Context { type PrefixType = Logger }

  def errorMessage(c: LoggerContext)(message: c.Expr[String]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isErrorEnabled)
        c.prefix.splice.handler(Error(message.splice))
    }

  def errorMessageCause(c: LoggerContext)(message: c.Expr[String], cause: c.Expr[Throwable]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isErrorEnabled)
        c.prefix.splice.handler(Error(message.splice, Some(cause.splice)))
    }

  def warnMessage(c: LoggerContext)(message: c.Expr[String]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isWarnEnabled)
        c.prefix.splice.handler(Warn(message.splice))
    }

  def warnMessageCause(c: LoggerContext)(message: c.Expr[String], cause: c.Expr[Throwable]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isWarnEnabled)
        c.prefix.splice.handler(Warn(message.splice, Some(cause.splice)))
    }

  def debugMessage(c: LoggerContext)(message: c.Expr[String]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isDebugEnabled)
        c.prefix.splice.handler(Debug(message.splice))
    }

  def debugMessageCause(c: LoggerContext)(message: c.Expr[String], cause: c.Expr[Throwable]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isDebugEnabled)
        c.prefix.splice.handler(Debug(message.splice, Some(cause.splice)))
    }

  def infoMessage(c: LoggerContext)(message: c.Expr[String]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isInfoEnabled)
        c.prefix.splice.handler(Info(message.splice))
    }

  def infoMessageCause(c: LoggerContext)(message: c.Expr[String], cause: c.Expr[Throwable]) =
    c.universe.reify {
      if (c.prefix.splice.backend.isInfoEnabled)
        c.prefix.splice.handler(Info(message.splice, Some(cause.splice)))
    }
}

object Logger {
  // For now the logger has a dedicated daemon thread
  implicit val S: Strategy = Strategy.Executor(java.util.concurrent.Executors.newSingleThreadExecutor(new ThreadFactory {
    def newThread(r: Runnable) = {
      val thread = Executors.defaultThreadFactory.newThread(r)
      thread.setDaemon(true)
      thread.setName("Journal-logger-daemon")
      thread
    }
  }))

  def apply[A](implicit A: Manifest[A]): Logger =
    apply(LoggerFactory.getLogger(A.runtimeClass))

  def apply(name: String): Logger = apply(LoggerFactory.getLogger(name))

  def apply(backend: Backend): Logger =
    new Logger(backend, Actor[LogMessage]({
      case Error(message, None) => backend.error(message)
      case Error(message, Some(e)) => backend.error(message, e)
      case Info(message, None) => backend.info(message)
      case Info(message, Some(e)) => backend.info(message, e)
      case Warn(message, None) => backend.warn(message)
      case Warn(message, Some(e)) => backend.warn(message, e)
      case Debug(message, None) => backend.debug(message)
      case Debug(message, Some(e)) => backend.debug(message, e)
    }))
}

