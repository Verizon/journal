
libraryDependencies ++= Seq(
  "org.slf4j"       % "slf4j-api"                 % "1.7.12",
  "ch.qos.logback"  % "logback-classic"           % "1.1.3",
  "org.scalaz"     %% "scalaz-core"               % "7.1.11",
  "org.scalaz"     %% "scalaz-concurrent"         % "7.1.11",
  "org.scala-lang"  % "scala-reflect"             % scalaVersion.value % "provided",
  "org.scalaz"     %% "scalaz-scalacheck-binding" % "7.1.11"  % "test",
  "org.scalacheck" %% "scalacheck"                % "1.13.4" % "test"
)

scalacOptions in Compile := Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:experimental.macros",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Xfuture",
  "-Xmax-classfile-name", (255 - 15).toString
)
