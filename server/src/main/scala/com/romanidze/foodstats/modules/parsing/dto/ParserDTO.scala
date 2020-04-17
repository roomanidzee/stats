package com.romanidze.foodstats.modules.parsing.dto

import java.time.LocalDateTime

/**
 * Case class for product information
 * @param recordTime time for product been parsed
 * @param id identificator of product
 * @param category category of product
 * @param title title of product
 * @param currentPrice current product price
 * @param position product position
 * @param weightInfo weight of product
 * @param productURL direct url for product
 *
 * @author Andrey Romanov
 */
case class ProductParsed(
  recordTime: LocalDateTime,
  id: Long,
  category: String,
  title: String,
  currentPrice: Long,
  position: Int,
  weightInfo: String,
  productURL: String
)

case class Results(
  results: List[ProductParsed]
)
