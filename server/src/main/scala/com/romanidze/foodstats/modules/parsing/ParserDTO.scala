package com.romanidze.foodstats.modules.parsing

import java.time.LocalDateTime

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
