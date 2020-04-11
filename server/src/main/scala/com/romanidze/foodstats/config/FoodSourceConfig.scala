package com.romanidze.foodstats.config

case class FoodSourceConfig(
  site: String,
  sections: List[String],
  pages: Int
)
