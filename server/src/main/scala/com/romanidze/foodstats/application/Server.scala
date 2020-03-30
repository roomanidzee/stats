package com.romanidze.foodstats.application

import cats.Applicative
import cats.effect._
import com.romanidze.foodstats.config.{ ApplicationConfig, ConfigurationLoader }
import com.romanidze.foodstats.modules.main.ApplicationModule
import fs2.Stream
import org.http4s.server.blaze.BlazeServerBuilder

object Server {

  val appConfig: ApplicationConfig = ConfigurationLoader.load
    .fold(
      e => sys.error(s"Failed to load configuration:\n${e.toList.mkString("\n")}"),
      identity
    )

  def launch[F[_]: ConcurrentEffect: Applicative: ContextShift: Timer]: Stream[F, ExitCode] = {

    val module = new ApplicationModule[F](appConfig)

    for {
      exitCode <- BlazeServerBuilder[F]
                   .bindHttp(appConfig.server.port, appConfig.server.host)
                   .withHttpApp(module.httpApp)
                   .serve
    } yield exitCode

  }

}
