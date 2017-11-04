name := "test-backend-iadvize"

version := "1.0"

scalaVersion := "2.12.4"


enablePlugins(DockerPlugin)

libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "9.4.7.v20170914"
libraryDependencies += "com.typesafe.scala-logging" % "scala-logging-slf4j_2.11" % "2.1.2"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.0"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.1"
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += "org.scalatra" %% "scalatra-specs2" % "2.5.3" % "test"
libraryDependencies += "org.scalatra" %% "scalatra-scalatest" % "2.5.3" % "test"

libraryDependencies += "org.scalatra"  %% "scalatra" % "2.5.3"
libraryDependencies += "org.scalatra"  %% "scalatra-json" % "2.5.3"
libraryDependencies += "org.scalatra"  %% "scalatra-swagger" % "2.5.3"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.5.0"

libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"
libraryDependencies += "com.github.tminglei" % "slick-pg_2.12" % "0.15.4"
libraryDependencies += "com.github.tminglei" %% "slick-pg_json4s" % "0.15.4"

buildOptions in docker := BuildOptions(
  cache = false
)

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("openjdk:8")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}