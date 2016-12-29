import sbt.Keys._
import sbt._

object BraintreeClient extends Build {

  lazy val PlayIntegrationTest = config("it").extend(Test)

  def itSettings = {
    sourceDirectories in IntegrationTest <+= baseDirectory( _ / "it" )
    sourceDirectory in IntegrationTest <<= baseDirectory( _ / "it" )
    scalaSource in IntegrationTest <<= baseDirectory ( _ / "it" )
  }

  lazy val commonSettings = Seq(
    sbtPlugin := true,
    organization := "com.andrewzurn",
    version := "0.1.1-SNAPSHOT",
    scalaVersion := "2.11.7",
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { x => false },
    resolvers ++= Seq(
      Resolver.mavenLocal,
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "Scalaz Bintray" at "https://dl.bintray.com/scalaz/releases"
    ),
    scalacOptions ++= Seq(
      "-deprecation",               // Emit warning and location for usages of deprecated APIs.
      "-feature",                   // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked",                 // Enable additional warnings where generated code depends on assumptions.
      "-Xfatal-warnings",           // Fail the compilation if there are any warnings.
      "-Xlint",                     // Enable recommended additional warnings.
      "-Ywarn-adapted-args",        // Warn if an argument list is modified to match the receiver.
      "-Ywarn-dead-code",           // Warn when dead code is identified.
      "-Ywarn-inaccessible",        // Warn about inaccessible types in method signatures.
      "-Ywarn-nullary-override",    // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
      "-Ywarn-numeric-widen"        // Warn when numerics are widened.
    )
  )

  lazy val root = Project(id = "braintree", base = file("."))
    .settings(commonSettings: _*).aggregate(braintreeClient, braintreeApi)

  lazy val client = Project(id="client", base = file("client"))
    .settings(commonSettings: _*) 

  lazy val api =  Project(id="api", base = file("api"))
    .settings(commonSettings: _*) 
    .dependsOn(client)
    .configs(PlayIntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(itSettings)
}
