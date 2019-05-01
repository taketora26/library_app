name := """library_app"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play"           % "4.0.1" % Test,
  "org.scalikejdbc"        %% "scalikejdbc"                  % "3.3.3",
  "org.scalikejdbc"        %% "scalikejdbc-config"           % "3.3.3",
  "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % "2.7.0-scalikejdbc-3.3",
  "org.scalikejdbc"        %% "scalikejdbc-test"             % "3.3.3" % "test",
  "ch.qos.logback"         % "logback-classic"               % "1.2.3" % Test,
  "mysql"                  % "mysql-connector-java"          % "6.0.6",
  "org.mockito"            % "mockito-core"                  % "2.27.0" % Test
)

enablePlugins(FlywayPlugin)

import com.typesafe.config.ConfigFactory

val conf = ConfigFactory.parseFile(new File("./conf/application.conf"))

flywayUrl := conf.getString("db.default.url")
flywayUser := conf.getString("db.default.user")
flywayPassword := conf.getString("db.default.password")
flywayLocations := Seq(
  "filesystem:./conf/db/migration/default"
)
flywayUrl in Test := conf.getString("test.db.default.url")
flywayUser in Test := conf.getString("test.db.default.user")
flywayPassword in Test := conf.getString("test.db.default.password")
flywayLocations in Test := Seq(
  "filesystem:./conf/db/migration/default"
)
