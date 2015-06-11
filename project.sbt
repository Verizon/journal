
organization in Global := "oncue.journal"

scalaVersion in Global := "2.10.5"

crossScalaVersions in Global := Seq("2.10.5", "2.11.6")

resolvers += Resolver.sonatypeRepo("releases")

lazy val journal = project.in(file(".")).aggregate(core).settings(publish := {})

lazy val core = project

lazy val docs = project.dependsOn(core)

lazy val benchmark = project.dependsOn(core)

publish := {}
