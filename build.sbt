name := "PlayScala1"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "org.scalatestplus" %% "play" % "1.1.0" % "test"

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;views.*;prod.*;app.*"
ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 100
ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := true
ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := true
parallelExecution in Test := false



