package com.romanidze.foodstats.modules.parsing

case class ProductParsed(
  id: Long,
  category: String,
  title: String,
  currentPrice: Long,
  photoURL: String
)
