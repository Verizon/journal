
import sbt._, Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._
import com.typesafe.sbt.pgp.PgpKeys._
import bintray.BintrayKeys._

object custom {
  def ignore = Seq(
    publish := (),
    publishSigned := (),
    publishLocal := (),
    publishLocalSigned := (),
    publishArtifact in Test := false,
    publishArtifact in Compile := false
  )

  def settings =
    bintraySettings ++
    releaseSettings ++
    publishingSettings

  def bintraySettings = Seq(
    bintrayPackageLabels := Seq("logging", "functional programming", "reasonable"),
    bintrayOrganization := Some("oncue"),
    bintrayRepository := "releases",
    bintrayPackage := "journal"
  )

  def releaseSettings = Seq(
    releaseCrossBuild := true,
    releasePublishArtifactsAction := publishSigned.value,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )

  def publishingSettings = Seq(
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
      </developers>),
    publishMavenStyle := true,
    useGpg := true,
    licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("http://oncue.github.io/journal")),
    scmInfo := Some(ScmInfo(url("https://github.com/oncue/journal"),
                            "git@github.com:oncue/journal.git")),
    pomIncludeRepository := { _ => false },
    publishArtifact in Test := false
  )

}
