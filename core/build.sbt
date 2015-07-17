
name := "core"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:experimental.macros"
)

libraryDependencies ++= Seq(
  "org.slf4j"       % "slf4j-api"                 % "1.7.12",
  "ch.qos.logback"  % "logback-classic"           % "1.1.3",
  "org.scalaz"     %% "scalaz-core"               % "7.1.2",
  "org.scalaz"     %% "scalaz-concurrent"         % "7.1.2",
  "org.scala-lang"  % "scala-reflect"             % scalaVersion.value % "provided",
  "org.scalaz"     %% "scalaz-scalacheck-binding" % "7.1.2"  % "test",
  "org.scalacheck" %% "scalacheck"                % "1.12.3" % "test"
)

releaseSettings

pomExtra := (
  <developers>
    <developer>
      <id>timperrett</id>
      <name>Timothy Perrett</name>
      <url>http://github.com/timperrett</url>
    </developer>
    <developer>
      <id>runarorama</id>
      <name>Runar Bjarnason</name>
      <url>http://github.com/runarorama</url>
    </developer>
    <developer>
      <id>stew</id>
      <name>Stew O'Connor</name>
      <url>http://github.com/stew</url>
    </developer>
  </developers>)

publishMavenStyle := true

licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html"))

homepage := Some(url("http://oncue.github.io/journal"))

scmInfo := Some(ScmInfo(url("https://github.com/oncue/journal"),
                            "git@github.com:oncue/journal.git"))

bintrayPackageLabels := Seq("logging", "functional programming", "reasonable")

bintrayOrganization := Some("oncue")

bintrayRepository := "releases"

bintrayPackage := "journal"
