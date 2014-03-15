name := "slick-dao"

version := "1.0"

scalaVersion := "2.10.3"


libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "com.typesafe" % "config" % "1.2.0",
  "org.slf4j" % "slf4j-api" % "1.6.4",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "com.h2database" % "h2" % "1.3.166",
  "c3p0" % "c3p0" % "0.9.1.2"
)