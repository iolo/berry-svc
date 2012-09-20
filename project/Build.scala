import sbt._
import Keys._
import akka.sbt.AkkaKernelPlugin
import akka.sbt.AkkaKernelPlugin.{ Dist, outputDirectory, distJvmOptions}

object BerryKernelBuild extends Build {
  lazy val BerryKernel = Project(
    id = "berry-kernel",
    base = file("."),
    settings = buildSettings ++ AkkaKernelPlugin.distSettings ++ Seq(
      distJvmOptions in Dist := "-Xms256M -Xmx1024M",
      outputDirectory in Dist := file("target/dist")
    )
  )

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "kr.iolo.berry",
    version      := "0.0.1",
    scalaVersion := "2.9.1",
    crossPaths   := false,
    organizationName := "IoloTheBard",
    organizationHomepage := Some(url("http://iolo.kr")),
    resolvers := Resolvers.all,
    libraryDependencies ++= Dependencies.all,
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
    javacOptions  ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")
  )
}

object Resolvers {
  val akka = "Akka Repo" at "http://repo.akka.io/releases/"
  val typesafe = "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  val spray = "Spray Repo" at "http://repo.spray.cc/"

  val all = Seq(akka, typesafe, spray)
}

object Dependencies {
  object V {
    val akka = "2.0.3"
    val scalatest = "1.8"
    val logback = "1.0.7"
    val jodaTime = "2.1"
    val jodaConvert = "1.2"
  }

  val akkaActor = "com.typesafe.akka" % "akka-actor" % V.akka
  val akkaRemote = "com.typesafe.akka" % "akka-remote" % V.akka
  val akkaKernel = "com.typesafe.akka" % "akka-kernel" % V.akka
  val akkaSlf4j = "com.typesafe.akka" % "akka-slf4j" % V.akka
  val akkaTestkit = "com.typesafe.akka" % "akka-testkit" % V.akka
  val scalatest = "org.scalatest" %% "scalatest" % V.scalatest % "test"
  val logback = "ch.qos.logback" % "logback-classic" % V.logback
  val jodaTime = "joda-time" % "joda-time" % V.jodaTime
  val jodaConvert = "org.joda" % "joda-convert" % V.jodaConvert

  val all = Seq(
    akkaActor, akkaRemote, akkaKernel, akkaSlf4j, akkaTestkit, scalatest, logback, jodaTime, jodaConvert
  )
}
