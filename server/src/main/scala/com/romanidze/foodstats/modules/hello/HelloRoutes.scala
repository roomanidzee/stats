package com.romanidze.foodstats.modules.hello

import cats.implicits._
import cats.effect.Sync

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HelloRoutes[F[_]: Sync]() extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "users" => Ok("Hello, user!")

  }

}
