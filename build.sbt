name := "test-backend-iadvize"

version := "1.0"

scalaVersion := "2.12.4"

javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.7.v20170914",
  //"com.typesafe.scala-logging" % "scala-logging-slf4j_2.11" % "2.1.2",
  "com.typesafe.play" %% "play-json" % "2.6.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scalatra"  %% "scalatra" % "2.5.3",
  "org.scalatra"  %% "scalatra-json" % "2.5.3",
  "org.scalatra"  %% "scalatra-swagger" % "2.5.3",
  "org.json4s"    %% "json4s-native" % "3.5.0",
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.postgresql" % "postgresql" % "42.1.4",
  "com.zaxxer" % "HikariCP" % "2.7.2",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.3.0",
  "com.github.tminglei" % "slick-pg_2.12" % "0.15.4",
  "com.github.tminglei" %% "slick-pg_joda-time" % "0.15.4",
  "com.github.tminglei" %% "slick-pg_json4s" % "0.15.4",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.15.4",
  "joda-time" % "joda-time" % "2.9.7",
  "org.joda" % "joda-convert" % "1.8.1"
)