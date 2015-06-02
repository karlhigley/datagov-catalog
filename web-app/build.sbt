name := """web-app"""

version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" % "angularjs" % "1.4.0",
  "org.webjars" % "requirejs" % "2.1.18"
)     

lazy val root = (project in file(".")).enablePlugins(PlayScala)

pipelineStages := Seq(rjs, digest, gzip)
