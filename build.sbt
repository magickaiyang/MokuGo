name := """mokugo"""
organization := "com.reprincipia"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"

val akkaHTTPVersion = "10.1.8"

// Akka HTTP dependencies used by Play
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHTTPVersion,
  // Add this one if you are using HTTP/2
  "com.typesafe.akka" %% "akka-http2-support" % akkaHTTPVersion
)
