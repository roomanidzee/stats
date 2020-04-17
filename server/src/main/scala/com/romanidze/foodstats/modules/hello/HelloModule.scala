package com.romanidze.foodstats.modules.hello

import cats.effect.{ Async, ContextShift }
import com.romanidze.foodstats.modules.hello.routes.HelloRoutes
import org.http4s.HttpRoutes

class HelloModule[F[_]: Async: ContextShift]() {

  val routes: HttpRoutes[F] = new HelloRoutes[F]().routes

}
