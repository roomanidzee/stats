package com.romanidze.foodstats.modules.parsing

import cats.effect._
import com.romanidze.foodstats.config.FoodSourceConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ParserService[F[_]: Sync: ContextShift](config: FoodSourceConfig, blocker: Blocker) {

  def parseData(body: String): F[List[ProductParsed]] = {

    blocker.delay {

      val document: Document = Jsoup.parse(body)

      val baseCSSQuery = "div.page > div.page__wrapper > section.main_content > div.content-wrapper" +
        " > section.products-block > section.products-block__wrapper.cf > div.products-block__content >" +
        " section.vm_product_list_block_view_container > div.products__list.products__list--grid"

      val blocks: Elements = document.select(baseCSSQuery)

      List()

    }

  }

}
