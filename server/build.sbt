name := "food-stats"

version := "0.0.1"

scalaVersion := "2.12.10"

resolvers ++= Seq(
  Resolver.mavenCentral,
  Resolver.mavenLocal,
  Resolver.bintrayRepo("sbt-assembly", "maven")
)

assemblyJarName in assembly := s"food-stats.jar"
mainClass in assembly := Some("com.romanidze.foodstats.application.Main")

assemblyMergeStrategy in assembly := {
  case x if x.contains("io.netty.versions.properties") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification"
)
maxErrors := 5

scalafmtOnCompile := true

libraryDependencies ++=
  Dependencies.mainDeps ++
    Dependencies.testDeps
