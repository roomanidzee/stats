name := "food-stats"

version := "0.0.1"

scalaVersion := "2.12.10"

resolvers ++= Seq(
  Resolver.mavenCentral,
  Resolver.mavenLocal,
  Resolver.bintrayRepo("sbt-assembly", "maven")
)

assemblyJarName in assembly := "food-stats.jar"
test in assembly := {}
mainClass in assembly := Some("com.romanidze.foodstats.application.Main")

assemblyMergeStrategy in assembly := {
  case x if x.contains("io.netty.versions.properties") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-o"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification"
)

scalafmtOnCompile := true

libraryDependencies ++=
  Dependencies.mainDeps ++
    Dependencies.testDeps

addCommandAlias("construct", ";jacoco;assembly")
