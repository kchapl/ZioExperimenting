ThisBuild / scalaVersion := "2.13.13"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

val zioVersion = "2.0.21"

lazy val root = (project in file("."))
  .settings(
    name := "ZioExperimenting",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "dev.zio" %% "zio-test" % zioVersion % Test,
      "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
      "dev.zio" %% "zio-mock" % "1.0.0-RC12" % Test,
      "com.softwaremill.sttp.client3" %% "zio" % "3.9.3",
      "com.lihaoyi" %% "requests" % "0.7.1",
    ),
  )

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
