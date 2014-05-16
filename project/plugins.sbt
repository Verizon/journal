// Comment to get more information during initialization
logLevel := Level.Warn

resolvers ++= Seq(
  "Sonatype Nexus Intel Media Maven Group" at "http://nexus.svc.oncue.com/nexus/content/groups/intel_media_maven/",
  Resolver.url("Sonatype Nexus Intel Media Ivy Group", url("http://nexus.svc.oncue.com/nexus/content/groups/intel_media_ivy/"))(Resolver.ivyStylePatterns)
)

libraryDependencies ++= Seq(
)

addSbtPlugin("com.intel.media" %% "mediabuild" % "2.0.+")

addSbtPlugin("eu.henkelmann" %% "junit_xml_listener" % "0.7.+")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
