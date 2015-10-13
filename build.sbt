name := """CarAdvertRest"""

scalaVersion := "2.11.7"

libraryDependencies ++= List(
  "com.github.seratch" %% "awscala" % "0.5.+",
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka"  %% "akka-testkit" % "2.3.9" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.mockito" % "mockito-core" % "1.10.19",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.4" % "test"
)

scalacOptions  ++= Seq(
	"-feature",
	"-encoding", "UTF-8"
)