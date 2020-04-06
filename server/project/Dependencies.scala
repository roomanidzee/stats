import sbt._

object Dependencies {


  private val versions: Map[String, String] = Map(

    "http4s" -> "0.21.2",

    "circe" -> "0.13.0",

    "pureconfig" -> "0.12.3",
    "scala-test" -> "3.1.1",

    "cats" -> "2.1.1",

    "jsoup" -> "1.13.1",

    "doobie" -> "0.8.8",
    "postgresql" -> "42.2.11",
    "liquibase" -> "3.8.8",

    "scala-logging" -> "3.9.2",
    "logback" -> "1.2.3",

    "flexmark" -> "0.35.10"

  )

  //http4s
  private val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-blaze-server" % versions("http4s"),
    "org.http4s" %% "http4s-blaze-client" % versions("http4s"),
    "org.http4s" %% "http4s-circe" % versions("http4s"),
    "org.http4s" %% "http4s-dsl" % versions("http4s")
  )

  //circe
  private val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-generic" % versions("circe"),
    "io.circe" %% "circe-literal" % versions("circe"),
    "io.circe" %% "circe-parser" % versions("circe")
  )

  //web
  private val web: Seq[ModuleID] = http4s.union(circe)

  //pureconfig
  private val pureConfig: Seq[ModuleID] = Seq(
    "com.github.pureconfig" %% "pureconfig"             % versions("pureconfig"),
    "com.github.pureconfig" %% "pureconfig-cats-effect" % versions("pureconfig")
  )

  //logging
  private val logging: Seq[ModuleID] = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % versions("scala-logging"),
    "ch.qos.logback" % "logback-classic" % versions("logback")
  )

  //cats
  private val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core" % versions("cats")
  )

  //doobie
  private val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-core" % versions("doobie"),
    "org.tpolecat" %% "doobie-postgres" % versions("doobie"),
    "org.tpolecat" %% "doobie-hikari" % versions("doobie")
  )

  //jsoup
  private val jsoup: Seq[ModuleID] = Seq(
    "org.jsoup" % "jsoup" % versions("jsoup")
  )

  //jdbc
  private val jdbc: Seq[ModuleID] = Seq(

    "org.postgresql" % "postgresql" % versions("postgresql"),
    "org.liquibase" % "liquibase-core" % versions("liquibase"),

  ).union(doobie)

  //tests
  private val scalaTest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % versions("scala-test"),
    "org.scalactic" %% "scalactic" % versions("scala-test")
  ).map(_ % Test)

  private val doobieTest: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-scalatest" % versions("doobie")
  ).map(_ % Test)

  private val flexmark: Seq[ModuleID] = Seq(
    "com.vladsch.flexmark" % "flexmark-all" % versions("flexmark")
  ).map(_ % Test)

  val mainDeps: Seq[ModuleID] =
    web.union(pureConfig)
      .union(logging)
      .union(cats)
      .union(jdbc)
      .union(jsoup)

  val testDeps: Seq[ModuleID] =
    scalaTest.union(doobieTest)
             .union(flexmark) 

}
