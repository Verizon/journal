
organization in Global := "oncue.journal"

crossScalaVersions in Global := Seq("2.10.5", "2.11.7")

scalaVersion in Global := crossScalaVersions.value.head

resolvers += Resolver.sonatypeRepo("releases")

lazy val journal = project.in(file(".")).aggregate(core).settings(publish := {})

lazy val core = project

lazy val docs = project.dependsOn(core)

lazy val benchmark = project.dependsOn(core)

custom.ignore
