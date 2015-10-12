name := """CarAdvertRest"""

scalaVersion := "2.11.7"

libraryDependencies ++= List(
  "com.github.seratch" %% "awscala" % "0.5.+",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

scalacOptions += "-feature"