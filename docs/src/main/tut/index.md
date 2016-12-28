---
layout: default
title:  "Home"
section: "home"
---

# Introduction

Journal is a fast, simple, Scala library for logging, based on SLF4J. It uses Logback as the default backend, but you can supply any backend you want.

## Getting Started

Using Journal is simple:

### Step 1: Add the dependency to your project

``` scala
libraryDependencies += "io.verizon.journal" %% "core" % "x.x.+"
```

(check for the latest release by [looking on Maven Central](https://maven-badges.herokuapp.com/maven-central/io.verizon.journal/core_2.11))

### Step 2: Configure the logger

You'll need the `logback.xml` file to actually tell the SLF4J implementation (Logback in this instance) what to do:

*logback.xml OR logger.xml*

``` xml
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
```

This is a very basic configuration. For more good stuff on the appender configuration, read the more detailed section below.

***Important***: Remember that at deploy time you need to ensure that you actually tell your running process where the `logback.xml` file is. The one you package in your JAR is likely to be suitable for local development and not a lot else. With this in mind, ensure you configure your deploy process to set the parameter below:

```
-Dlogback.configurationFile=/path/to/logback.xml
```

### Step 3: Log your little heart out!

With those setup steps done, you're good to go. Now you can use the logger within your code. Here's a short example:

``` scala
import journal._

object Foo {
  val log = Logger[this.type]
  def main(args: Array[String]): Unit = {
    log.info("COWS!")
    // Your application code here
  }
}
```

That's all there is to it.

<a name="log-levels"></a>

# Log Levels

There's a range of methods on the log instance (in order of log level precedence):

``` scala
error(msg: => String): Unit
error(msg: => String, t: => Throwable): Unit

info(msg: => String): Unit
info(msg: => String, t: => Throwable): Unit

warn(msg: => String): Unit
warn(msg: => String, t: => Throwable): Unit

debug(msg: => String): Unit
debug(msg: => String, t: => Throwable): Unit
```

This is the standard fare in terms of log levels. There is no "trace" level because it's not often clear what "trace" offers over "debug" and people get the two confused. So we just excluded it altogether.

There are no methods to check whether certain log levels are enabled. You should never need to do that. The logger is implemented using macros to ensure that it has _extremely low overhead_ if the log level in question is not enabled. So you can log as much as you like without affecting performance.

<a name="threading"></a>

# Asynchronous logging

Journal originally used an actor internally to make the logging of messages asynchronous, but since it was created, logging backends have gained their own ability to support asynchronous logging. So journal is no longer asynchronous, we expect that if you need asynchronous logging, you will configure that in your logging backend. With logback, this is accomplished by attaching an AsyncAppender to some other synchronous appender. For example:

```xml
<configuration>
  <appender name="FILE" class="ch.qos.logback.core.FileAppender">
    <file>/var/log/my.log</file>
    <encoder>
      <pattern>%logger{35} - %msg%n</pattern>
    </encoder>
  </appender>

  <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="FILE" />
  </appender>

  <root level="DEBUG">
    <appender-ref ref="ASYNC" />
  </root>
</configuration>
```

Read more about the AsyncAppender [in the logback documentation](http://logback.qos.ch/manual/appenders.html#AsyncAppender).

<a name="migration"></a>

# Migration & Improvements

If you're migrating from another logging system, the API has the following differences:

* Arbitrary arguments for log notation have been removed now that Scala supports macros. For example:
`log.debug("this is foo: {}", foo)` is a common logging pattern in other systems, would be replaced by: `log.debug(s"this is foo: $foo")`. To read more about Scala string interpolation, please [visit the language documentation site](http://docs.scala-lang.org/overviews/core/string-interpolation.html).

* When creating Logger instances, explicit naming is now supported, along with type names. Both of these *are* valid: `Logger[Foo]`, `Logger[this.type]` and `Logger("qux")`.

