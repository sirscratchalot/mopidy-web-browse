name := """Mopidy-web-browse"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
        jdbc,
        ehcache,
        ws,
        guice,
        "org.jmdns" % "jmdns" % "3.5.1" withSources() withJavadoc(),
        "org.apache.derby" % "derby" % "10.13.1.1",
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test withSources() withJavadoc(),
        "org.mockito" % "mockito-core" %"2.13.0" %Test,
        )
libraryDependencies += "io.backchat.hookup" %% "hookup" % "0.4.2" %Test

