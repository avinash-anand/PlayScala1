name := "PlayScala1"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "org.scalatestplus" %% "play" % "1.1.0" % "test"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
  "org.jsoup" % "jsoup" % "1.8.2",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"

)

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;views.*;prod.*;app.*;forms.*;models.*;"
ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 100
ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := true
ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := true
ScoverageSbtPlugin.ScoverageKeys.coverageOutputHTML := true
parallelExecution in Test := false

wartremoverWarnings ++= Warts.unsafe
//wartremoverExcluded += baseDirectory.value / "src" / "main" / "scala" / "SomeFile.scala"
wartremoverExcluded += baseDirectory.value / "conf" / "prod.routes"
