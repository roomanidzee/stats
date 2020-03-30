package com.romanidze.foodstats.modules.main

import cats.data.Kleisli
import cats.implicits._
import cats.effect.{ Async, Concurrent, ContextShift }
import com.romanidze.foodstats.config.ApplicationConfig
import com.romanidze.foodstats.modules.hello.HelloModule
import org.http4s.server.Router
import org.http4s.syntax.kleisli._
import org.http4s.{ Headers, Http, Request, Response }
import org.http4s.server.middleware.{ CORS, Logger }

class ApplicationModule[F[_]: Concurrent: Async: ContextShift](config: ApplicationConfig) {

  private val helloModule = new HelloModule[F]()

  private val routes = helloModule.routes

  private val router: Kleisli[F, Request[F], Response[F]] =
    Router[F](
      config.server.prefix -> routes
    ).orNotFound

  private val loggedRouter = Logger.httpApp[F](
    logHeaders = true,
    logBody = true,
    Headers.SensitiveHeaders.contains
  )(router)

  val httpApp: Http[F, F] = CORS(loggedRouter)

}
