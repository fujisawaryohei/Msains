name := """msains"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.191",
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "com.ejisan" %% "kuro-otp" % "0.0.1-SNAPSHOT"
)
libraryDependencies += guice
resolvers += "EJI" at "https://ejisan.github.io/repo/"
