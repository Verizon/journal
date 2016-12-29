
organization in Global := "io.verizon.journal"

crossScalaVersions in Global := Seq("2.12.1", "2.11.8", "2.10.6")

scalaVersion in Global := crossScalaVersions.value.head

lazy val journal = project.in(file(".")).aggregate(core, docs)

lazy val core = project

lazy val docs = project.dependsOn(core)

lazy val benchmark = project.dependsOn(core)

enablePlugins(DisablePublishingPlugin)
