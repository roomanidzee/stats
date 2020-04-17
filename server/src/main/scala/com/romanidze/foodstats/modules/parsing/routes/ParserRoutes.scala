package com.romanidze.foodstats.modules.parsing.routes

import cats.{ Applicative, Parallel }
import cats.implicits._
import cats.effect._
import com.romanidze.foodstats.config.FoodSourceConfig
import com.romanidze.foodstats.modules.parsing.dto.{ ParserDTOSerializing, Results }
import com.romanidze.foodstats.modules.parsing.services.ParserService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

/**
 * Http routes for product direct parsing
 * @param config products parsing config
 * @param service service for retrieving data
 *
 * @author Andrey Romanov
 *
 */
class ParserRoutes[F[_]: Async: Applicative: Parallel](
  config: FoodSourceConfig,
  service: ParserService[F]
) extends Http4sDsl[F]
    with ParserDTOSerializing {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "products" / "parsed" =>
      for {
        htmlBody           <- service.retrieveHTML(config)
        htmlBodyMapped     = htmlBody.map(elem => elem.body.right.get)
        productsData       <- htmlBodyMapped.parTraverse(service.parseData)
        productsDataMapped = Results(productsData.flatten)
        result             <- Ok(productsDataMapped)
      } yield result

  }

}
