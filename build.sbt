name := """OntoPlay-TAN"""

version := "1.0-SNAPSHOT"

lazy val module = (project in file("OntoPlay"))
    .enablePlugins(PlayJava)

lazy val root = (project in file("."))
	.enablePlugins(PlayJava)
	.aggregate(module)
    .dependsOn(module)

scalaVersion := "2.11.7"

routesGenerator := StaticRoutesGenerator

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)
