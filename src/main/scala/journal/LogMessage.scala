package journal

sealed trait LogMessage
case class Error(message: String, cause: Option[Throwable] = None) extends LogMessage
case class Info(message: String, cause: Option[Throwable] = None) extends LogMessage
case class Warn(message: String, cause: Option[Throwable] = None) extends LogMessage
case class Debug(message: String, cause: Option[Throwable] = None) extends LogMessage
