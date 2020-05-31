ThisBuild / name := """library_app"""
ThisBuild / organization := "com.example"
ThisBuild / version := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.2"

import com.typesafe.config.ConfigFactory
val conf = ConfigFactory.parseFile(new File("./conf/application.conf"))

val scalikejdbcVer = "3.4.2"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, FlywayPlugin)
  .settings(
    libraryDependencies ++= Seq(
        guice,
        "org.scalatestplus.play" %% "scalatestplus-play"           % "5.1.0"        % Test,
        "org.scalikejdbc"        %% "scalikejdbc"                  % scalikejdbcVer,
        "org.scalikejdbc"        %% "scalikejdbc-config"           % scalikejdbcVer,
        "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.4",
        "org.scalikejdbc"        %% "scalikejdbc-test"             % scalikejdbcVer % Test,
        "ch.qos.logback"          % "logback-classic"              % "1.2.3"        % Test,
        "mysql"                   % "mysql-connector-java"         % "8.0.20",
        "org.mockito"             % "mockito-core"                 % "3.3.3"        % Test,
        "org.scalatest"          %% "scalatest"                    % "3.1.2"        % Test
      ),
    flywayUrl := conf.getString("db.default.url"),
    flywayUser := conf.getString("db.default.user"),
    flywayPassword := conf.getString("db.default.password"),
    flywayLocations := Seq(
        "filesystem:./conf/db/migration/default"
      ),
    Test / flywayUrl := conf.getString("test.db.default.url"),
    Test / flywayUser := conf.getString("test.db.default.user"),
    Test / flywayPassword := conf.getString("test.db.default.password"),
    Test / flywayLocations := Seq(
        "filesystem:./conf/db/migration/default"
      ),
    test.in(Test) := {
      test
        .in(Test)
        .dependsOn(flywayMigrate.in(Test))
        .dependsOn(flywayClean.in(Test))
        .value
    }
  )
