

name := "akka-prac1"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++=  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.9",
    "com.typesafe.akka"   %%  "akka-testkit"  % "2.4.9",
    "org.scalatest" %% "scalatest" % "3.0.0"
     )




lazy val root = (project in file(".")).enablePlugins(PlayScala)

