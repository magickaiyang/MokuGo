name := """mokugo"""
organization := "com.reprincipia"

version := "2.3"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += "com.google.firebase" % "firebase-admin" % "6.8.1"
