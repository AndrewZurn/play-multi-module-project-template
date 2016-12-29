import com.typesafe.sbt.packager.docker._

name := """zalude-braintree-payments-api"""

enablePlugins(DockerPlugin)
enablePlugins(PlayScala)
enablePlugins(JavaServerAppPackaging)

libraryDependencies ++= Seq(
  jdbc,
  filters,
  cache,
  "com.braintreepayments.gateway" % "braintree-java" % "2.53.0",
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "org.postgresql" % "postgresql" % "9.4-1204-jdbc42",
  "com.github.tminglei" %% "slick-pg" % "0.10.1",
  "com.github.tminglei" %% "slick-pg_joda-time" % "0.10.1",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.10.1",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test,it",
  "org.scalatestplus" %% "play" % "1.4.0-M4" % "test,it",
  "org.mockito" % "mockito-core" % "1.10.19" % "test"
)

packageName in Docker := packageName.value
version in Docker := version.value
dockerBaseImage := "awzurn/zalude-play-framework-docker"
//dockerRepository := Some("gcr.io/zalude-mobile")
dockerRepository := Some("awzurn")
dockerExposedPorts := Seq(9000, 8080)

// Settings for Play
routesGenerator := InjectedRoutesGenerator

// Disable parallel execution of tests
parallelExecution in Test := false

// excluded classes from test coverage
coverageExcludedPackages := "<empty>;router\\.*;.*RepositoryModule.*;.*ServiceModule.*;.*repository\\.*"

val playConfig = Option(System.getProperty("config.resource")) match {
  case Some(p) => ("application.dist.conf", "logback.dist.xml")
  case None => ("application.conf", "logback.xml")
}

herokuJdkVersion in Compile := "1.8"

herokuAppName in Compile := Map(
  "dev" -> "braintree-client-api-dev",
  "prod"  -> "braintree-client-api"
).getOrElse(sys.props("appEnv"), "braintree-client-api-dev")

herokuConfigVars in Compile := Map(
  "JAVA_OPTS" -> "-Xmx384m -Xss512k -XX:+UseCompressedOops"
)

javaOptions in Universal ++= Seq(
  s"-Dconfig.resource=${playConfig._1}",
  s"-Dlogger.resource=${playConfig._2}",
  "-Dpidfile.path=/dev/null",
  s"-Dhttp.port=8080"
)
