## Journal

Journal is a fast, simple, asynchronous Scala library for logging, based on SLF4J. It uses Logback as the default backend, but you can supply any backend you want.

### Quick Start

Using Journal is simple:

#### Step 1: Add the dependency to your project

````
libraryDependencies += "oncue.svc" %% "journal" % "x.x.x"
````

#### Step 2: Configure the logger

You'll need the `logback.xml` file to actually tell the SLF4J implementation (Logback in this instance) what to do:


*logback.xml OR logger.xml*

````
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="false" debug="false">
  <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%highlight([%level]) %logger{10} %X{example} %X{another}  - %msg%n</pattern>
    </encoder>
  </appender>
  <root level="DEBUG">
    <appender-ref ref="stdout" />
  </root>
</configuration>
````

This is a very basic configuration. For more good stuff on the appender configuration, read the more detailed section below.

#### Step 3: Log your little heart out!

With those setup steps done, you're good to go. Now you can use the logger within your code. Here's a short example:

````
import journal._

object Foo {
  lazy val log = Logger[Foo]
  def main(args: Array[String]): Unit = {
    log.info("COWS!")
    // Your application code here
  }
}
````

That's all there is to it. By way of style, we recommend always specifying your logger as a `lazy val`. The reason is that if you never end up using the logger instance (e.g. if your application has no errors to log), the logger will never be initialized.

### Logger daemon threads ###

There is no need to shut down your logger at the end. The logger uses a bounded pool of daemon threads that will be shut down automatically when your program terminates.

Because it uses a thread pool, calling the methods on the logger _does not block_ your own thread. It returns control back to you immediately.

There's a range of methods on the log instance (in order of log level precedence):

````
error(msg: => String): Unit
error(msg: => String, t: => Throwable): Unit

info(msg: => String): Unit
info(msg: => String, t: => Throwable): Unit

warn(msg: => String): Unit
warn(msg: => String, t: => Throwable): Unit

debug(msg: => String): Unit
debug(msg: => String, t: => Throwable): Unit
````

This is the standard fare in terms of log levels. There is no "trace" level because it's not often clear what "trace" offers over "debug" and people get the two confused. So we just excluded it altogether.

There are no methods to check whether certain log levels are enabled. You should never need to do that. The logger is implemented using macros to ensure that it has _no overhead at all_ if the log level in question is not enabled. So you can log as much as you like without affecting performance.


