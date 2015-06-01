---
layout: default
title:  "Home"
section: "home"
---

# Introduction

Journal is a fast, simple, asynchronous Scala library for logging, based on SLF4J. It uses Logback as the default backend, but you can supply any backend you want.

## Getting Started

Using Journal is simple:

### Step 1: Add the dependency to your project

```
libraryDependencies += "oncue.svc.journal" %% "core" % "x.x.+"
```
(check for the latest release by [looking on the nexus](http://nexus.svc.oncue.com/nexus/content/repositories/releases/oncue/svc/journal/core_2.10/))

### Step 2: Configure the logger

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

***Important***: Remember that at deploy time you need to ensure that you actually tell your running process where the `logback.xml` file is. The one you package in your JAR is likely to be suitable for local development and not a lot else. With this in mind, ensure you configure your deploy process to set the parameter below (see the [catapult documentation](https://github.svc.oncue.com/pages/IntelMedia/catapult) 
which explains how to customise the launch options inside an RPM)

```
-Dlogback.configurationFile=/path/to/logback.xml
``` 

### Step 3: Log your little heart out!

With those setup steps done, you're good to go. Now you can use the logger within your code. Here's a short example:

```
import journal._

object Foo {
  lazy val log = Logger[Foo]
  def main(args: Array[String]): Unit = {
    log.info("COWS!")
    // Your application code here
  }
}
```

That's all there is to it. By way of style, we recommend always specifying your logger as a `lazy val`. The reason is that if you never end up using the logger instance (e.g. if your application has no errors to log), the logger will never be initialised.

<a name="log-levels"></a>

# Log Levels

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

<a name="threading"></a>

# Threading

There is no need to shut down your logger at the end. The logger uses a bounded pool of daemon threads that will be shut down automatically when your program terminates.

Because it uses a thread pool, calling the methods on the logger _does not block_ your own thread. It returns control back to you immediately.

<a name="migration"></a>

# Migration & Improvements

If you're migrating from the "common util" logger, the API has the following differences:

* There is no longer any need to call `Logger.shutdown()`

* The arbitrary arguments for log notation have been removed now that Scala supports macros. For example:
`log.debug("this is foo: {}", foo)` in the common util logger, would be replaced by: `log.debug(s"this is foo: $foo")`. To read more about Scala string interpolation, please [visit the language documentation site](http://docs.scala-lang.org/overviews/core/string-interpolation.html).

* When creating Logger instances, explicit naming is now supported, along with type names. Both of these *are* valid now: `Logger[Foo]` and `Logger("qux")`.

* `Logger` internals no longer are as lazy as they were, so initialisation errors should be highly infrequent.



