name := "Terraforming"

version := "1.0.9"

scalaVersion := "2.10.2"

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "commons-cli" % "commons-cli" % "1.2",
  "com.google.guava" % "guava" % "14.0.1",
  "jp.ac.waseda" % "GameAIArena" % "1.7.1",
  "org.json4s" %% "json4s-native" % "3.2.4",
  "junit" % "junit" % "4.10" % "test",
  "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
  "org.specs2" % "specs2_2.10" % "2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"
)

seq(ScctPlugin.instrumentSettings : _*)
