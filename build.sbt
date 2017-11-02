name := "test-backend-iadvize"

version := "1.0"

scalaVersion := "2.12.1"

javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.7.v20170914",
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.11" % "2.1.2",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scalatra"  %% "scalatra" % "2.5.3",
  "org.scalatra"  %% "scalatra-json" % "2.5.3",
  "org.scalatra"  %% "scalatra-swagger" % "2.5.3",
  "org.json4s"    %% "json4s-native" % "3.5.0"
)