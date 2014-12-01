
import oncue.build._

OnCue.baseSettings

organization := "oncue.svc.journal"

name := "core"

scalaVersion := "2.10.4"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:experimental.macros"
)

libraryDependencies ++= Seq(
  "org.scala-lang"  % "scala-reflect"             % "2.10.4",
  "org.slf4j"       % "slf4j-api"                 % "1.7.+",
  "ch.qos.logback"  % "logback-classic"           % "1.0.+",
  "org.scalaz"     %% "scalaz-core"               % "7.1.0",
  "org.scalaz"     %% "scalaz-concurrent"         % "7.1.0",
  "org.scalaz"     %% "scalaz-scalacheck-binding" % "7.1.0" % "test",
  "org.scalacheck" %% "scalacheck"                % "1.10.1" % "test"
)
