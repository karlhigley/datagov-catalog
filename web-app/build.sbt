name := """web-app"""

version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" % "angularjs" % "1.4.0",
  "org.webjars" % "requirejs" % "2.1.18"
)     

lazy val root = (project in file(".")).enablePlugins(PlayScala)

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
