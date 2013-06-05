name := "Terraforming"

version := "1.0.0"

scalaVersion := "2.9.2"

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

libraryDependencies += "commons-cli" % "commons-cli" % "1.2"

libraryDependencies += "com.google.guava" % "guava" % "14.0.1"

libraryDependencies += "jp.ac.waseda.cs.washi" % "GameAIArena" % "1.4.4"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.1" % "test"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.0-rc1" % "test"

libraryDependencies += "org.scala-tools.testing" % "specs_2.9.3" % "1.6.9" % "test"

libraryDependencies += "org.scalatest" % "scalatest_2.9.3" % "1.9.1" % "test"

seq(ScctPlugin.instrumentSettings : _*)
