
libraryDependencies ++= Seq(
  "org.slf4j"       % "slf4j-api"                 % "1.7.12",
  "ch.qos.logback"  % "logback-classic"           % "1.1.3",
  "org.scalaz"     %% "scalaz-core"               % "7.1.2",
  "org.scalaz"     %% "scalaz-concurrent"         % "7.1.2",
  "org.scala-lang"  % "scala-reflect"             % scalaVersion.value % "provided",
  "org.scalaz"     %% "scalaz-scalacheck-binding" % "7.1.2"  % "test",
  "org.scalacheck" %% "scalacheck"                % "1.12.3" % "test"
)

scalacOptions += "-language:experimental.macros"