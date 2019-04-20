name := """mokugo"""
organization := "com.reprincipia"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
