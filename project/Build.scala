import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

object Build extends sbt.Build {

  lazy val root = Project("onecache-root", file("."))
    .aggregate(onecacheCore)
    .settings(basicSettings: _*)
    .settings(noPublishing: _*)

 lazy val onecacheCore = Project("onecache-core", file("onecache-core"))
    .settings(basicSettings: _*)
    .settings(formatSettings: _*)
    .settings(releaseSettings: _*)
    .settings(libraryDependencies ++= Dependencies.all)

  lazy val examples = Project("onecache-examples", file("examples"))
    .settings(exampleSettings: _*)

  lazy val basicSettings = Seq(
      organization := "com.wandoulabs.onecache",
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.10.4",
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xcheckinit"),
      resolvers ++= Seq(
        "typesafe repo" at "http://repo.typesafe.com/typesafe/releases/",
        "spray" at "http://repo.spray.io",
        "spray nightly" at "http://nightlies.spray.io/")
    )

  lazy val exampleSettings = basicSettings ++ noPublishing

  lazy val releaseSettings = Seq(
      publishTo := {
        val nexus = "https://oss.sonatype.org/"
        if (version.value.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      },
      publishMavenStyle := true,
      publishArtifact in Test := false,
      pomIncludeRepository := { (repo: MavenRepository) => false },
      pomExtra := (
        <url>https://github.com/wandoulabs/onecache</url>
        <licenses>
          <license>
            <name>The Apache Software License, Version 2.0</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:wandoulabs/onecache.git</url>
          <connection>scm:git:git@github.com:wandoulabs/onecache.git</connection>
        </scm>
        <developers>
          <developer>
            <id>cowboy129</id>
            <name>Xingrun CHEN</name>
            <email>cowboy129@gmail.com</email>
          </developer>
        </developers>
        )
    )

  lazy val noPublishing = Seq(
    publish := (),
    publishLocal := (),
    // required until these tickets are closed https://github.com/sbt/sbt-pgp/issues/42,
    // https://github.com/sbt/sbt-pgp/issues/36
    publishTo := None
  )

  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test := formattingPreferences)

  import scalariform.formatter.preferences._
  def formattingPreferences =
    FormattingPreferences()
      .setPreference(RewriteArrowSymbols, false)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(IndentSpaces, 2)
  
}

object Dependencies {
  val AKKA_VERSION = "2.3.2"

  val akka_actor = "com.typesafe.akka" %% "akka-actor" % AKKA_VERSION
  val akka_testkit = "com.typesafe.akka" %% "akka-testkit" % AKKA_VERSION % "test"
  val scalatest = "org.scalatest" %% "scalatest" % "2.0" % "test"

  val all = Seq(akka_actor, scalatest, akka_testkit)

}
