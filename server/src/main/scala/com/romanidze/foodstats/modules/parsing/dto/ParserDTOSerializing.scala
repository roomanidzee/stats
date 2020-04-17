package com.romanidze.foodstats.modules.parsing.dto

import java.time.format.DateTimeFormatter

import cats.Applicative
import cats.effect.IO
import io.circe.{ Encoder, Json }
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.circe.jsonEncoderOf
import org.http4s.EntityEncoder

/**
 * Serializing for ParserDTO instances
 * @author Andrey Romanov
 */
trait ParserDTOSerializing {

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

  implicit val productEncoder: Encoder[ProductParsed] = (obj: ProductParsed) =>
    Json.obj(
      ("record_time", Json.fromString(obj.recordTime.format(formatter))),
      ("id", Json.fromLong(obj.id)),
      ("category", Json.fromString(obj.category)),
      ("title", Json.fromString(obj.title)),
      ("current_price", Json.fromLong(obj.currentPrice)),
      ("position", Json.fromInt(obj.position)),
      ("weight_info", Json.fromString(obj.weightInfo)),
      ("product_url", Json.fromString(obj.productURL))
    )

  implicit def productEntityEncoder[F[_]: Applicative]: EntityEncoder[F, ProductParsed] =
    jsonEncoderOf
  implicit val productIOEncoder: EntityEncoder[IO, ProductParsed] = jsonEncoderOf[IO, ProductParsed]

  implicit val productsResultEncoder: Encoder.AsObject[Results] = deriveEncoder[Results]
  implicit def productsResultEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Results] =
    jsonEncoderOf
  implicit val productsResultIOEncoder: EntityEncoder[IO, Results] = jsonEncoderOf[IO, Results]

}

object ParserDTOSerializing extends ParserDTOSerializing
