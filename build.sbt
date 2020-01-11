name := """library_app"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"
lazy val scalikejdbcVersion = "3.4.0"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play"           % "5.0.0" % Test,
  "org.scalikejdbc"        %% "scalikejdbc"                  % scalikejdbcVersion,
  "org.scalikejdbc"        %% "scalikejdbc-config"           % scalikejdbcVersion,
  "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % "2.7.1-scalikejdbc-3.4",
  "org.scalikejdbc"        %% "scalikejdbc-test"             % scalikejdbcVersion % "test",
  "ch.qos.logback"         % "logback-classic"               % "1.2.3" % Test,
  "mysql"                  % "mysql-connector-java"          % "6.0.6",
  "org.mockito"            % "mockito-core"                  % "3.2.4" % Test
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
test.in(Test) := {
  test
    .in(Test)
    .dependsOn(flywayMigrate.in(Test))
    .dependsOn(flywayClean.in(Test))
    .value
}
