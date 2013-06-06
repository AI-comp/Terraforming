name := "Terraforming"

version := "1.0.0"

scalaVersion := "2.9.2"

libraryDependencies += "commons-cli" % "commons-cli" % "1.2"

libraryDependencies += "jp.ac.waseda.cs.washi" % "GameAIArena" % "1.4.4"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.1" % "test"

libraryDependencies += "org.specs2" %% "specs2" % "1.12.4.1" % "test"

seq(ScctPlugin.instrumentSettings : _*)
