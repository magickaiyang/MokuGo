name := """mokugo"""
organization := "com.reprincipia"

version := "1.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies ++= Seq("com.newrelic.agent.java" % "newrelic-agent" % "5.0.0")
libraryDependencies ++= Seq("com.newrelic.agent.java" % "newrelic-api" % "5.0.0")
