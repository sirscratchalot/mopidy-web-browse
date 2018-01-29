name := """local-discover"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.jmdns" % "jmdns" % "3.5.1" withSources() withJavadoc(),
  "org.apache.derby" % "derby" % "10.13.1.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test withSources() withJavadoc(),
    "org.mockito" % "mockito-core" %"2.13.0" %Test,
"io.backchat.hookup" %% "hookup" % "0.4.2" %Test
)


