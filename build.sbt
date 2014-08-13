import AssemblyKeys._

assemblySettings

jarName in assembly := "ContadorAlumnos.jar"

name := "contadorScala"

scalaVersion := "2.10.4"

version := "2.0"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10+"

libraryDependencies += "org.apache.poi" % "poi" % "3.10-FINAL"

libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.10-FINAL"