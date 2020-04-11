package com.romanidze.foodstats.modules.parsing

import java.time.{ LocalDateTime, ZoneId }

import cats.effect._
import com.romanidze.foodstats.config.FoodSourceConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.{ Document, Element }
import org.jsoup.select.Elements

import scala.collection.mutable.ListBuffer

class ParserService[F[_]: Sync: ContextShift](config: FoodSourceConfig, blocker: Blocker) {

  def parseData(body: String): F[List[ProductParsed]] = {

    blocker.delay {

      val document: Document                    = Jsoup.parse(body)
      val resultData: ListBuffer[ProductParsed] = ListBuffer.empty[ProductParsed]

      val baseCSSQuery = "div.page > div.page__wrapper > section.main_content > div.content-wrapper" +
            " > section.products-block > section.products-block__wrapper.cf > div.products-block__content >" +
            " section.vm_product_list_block_view_container > div.products__list.products__list--grid > article.products_item"

      val productInfoQuery = "div.products__item-desc > div.products__item-desc-wrapper " +
            "> div.products__item-title > a.products__item-link"

      val blocks: Elements = document.select(baseCSSQuery)

      blocks.forEach(elem => {

        val productData: Element = elem.children().first()

        val productInfo: Elements = productData.select(productInfoQuery)

        productInfo.forEach(productElem => {

          val title     = productElem.attr("data-gaprtitle")
          val titleInfo = title.split(",")

          resultData += ProductParsed(
            recordTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")),
            id = productElem.attr("data-gaproductid").toLong,
            category = productElem.attr("data-gaprcat"),
            title = titleInfo(0),
            weightInfo = titleInfo(1),
            currentPrice = productElem.attr("data-gaprprice").toLong,
            position = productElem.attr("dataga-position").toInt,
            productURL = productElem.attr("href")
          )

        })

      })

      resultData.toList

    }

  }

}
