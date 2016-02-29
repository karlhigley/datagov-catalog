name := """web-app"""

version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" % "angularjs" % "1.4.0",
  "org.webjars" % "requirejs" % "2.1.18",
  "com.sksamuel.elastic4s" %% "elastic4s" % "1.5.12",
  "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "1.5.12"
)     

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

pipelineStages := Seq(rjs, digest, gzip)


// Docker config
dockerBaseImage := "java:8u45"

lazy val util = (project in file("util")).
  enablePlugins(DockerPlugin).
  settings(    
    dockerExposedPorts := Seq(9000, 9443),
    version := "0.0.1",
    maintainer := "Karl Higley <kmhigley@gmail.com>",
    packageSummary := "A searchable Data.gov catalog",
    packageDescription := "Data.gov Catalog web app"
  )
