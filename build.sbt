name := """OntoPlay"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.code.gson" % "gson" % "2.2.2",
    "commons-io" % "commons-io" % "2.4"
)
