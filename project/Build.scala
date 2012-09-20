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
  val Akka = "Akka Repo" at "http://repo.akka.io/releases/"
  val Typesafe = "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  val Spray = "Spray Repo" at "http://repo.spray.cc/"

  val all = Seq(Akka, Typesafe, Spray)
}

object Dependencies {
  object V {
    val Akka = "2.0.3"
    val ScalaTest = "1.8"
    val Logback = "1.0.7"
    val JodaTime = "2.1"
    val JodaConvert = "1.2"
  }

  val akkaActor = "com.typesafe.akka" % "akka-actor" % V.Akka
  val akkaRemote = "com.typesafe.akka" % "akka-remote" % V.Akka
  val akkaKernel = "com.typesafe.akka" % "akka-kernel" % V.Akka
  val akkaSlf4j = "com.typesafe.akka" % "akka-slf4j" % V.Akka
  val akkaTestkit = "com.typesafe.akka" % "akka-testkit" % V.Akka
  val scalaTest = "org.scalatest" %% "scalatest" % V.ScalaTest % "test"
  val logback = "ch.qos.logback" % "logback-classic" % V.Logback
  val jodaTime = "joda-time" % "joda-time" % V.JodaTime
  val jodaConvert = "org.joda" % "joda-convert" % V.JodaConvert

  val all = Seq(
    akkaActor, akkaRemote, akkaKernel, akkaSlf4j, akkaTestkit, scalaTest, logback, jodaTime, jodaConvert
  )
}
