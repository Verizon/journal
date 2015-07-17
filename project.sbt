
// had to swap over to com.verizon due to maven central policy
organization in Global := "com.verizon.journal"

crossScalaVersions in Global := Seq("2.10.5", "2.11.6")

scalaVersion in Global := crossScalaVersions.value.head

resolvers += Resolver.sonatypeRepo("releases")

lazy val journal = project.in(file(".")).aggregate(core).settings(publish := {})

lazy val core = project

lazy val docs = project.dependsOn(core)

lazy val benchmark = project.dependsOn(core)

custom.ignore
