package com.romanidze.foodstats.modules.parsing.services

import java.time.{ LocalDateTime, ZoneId }

import cats._
import cats.effect._
import cats.implicits._
import com.romanidze.foodstats.config.FoodSourceConfig
import com.romanidze.foodstats.modules.parsing.dto.ProductParsed
import com.softwaremill.sttp._
import com.softwaremill.sttp.asynchttpclient.cats.AsyncHttpClientCatsBackend
import org.jsoup.Jsoup
import org.jsoup.nodes.{ Document, Element }
import org.jsoup.select.Elements

import scala.collection.mutable.ListBuffer

/**
 * Service for working for data from products site
 * @tparam F type variable for tagless instances
 * @author Andrey Romanov
 */
class ParserService[F[_]: Applicative: Async: ContextShift] {

  /**
   * Method for retrieving html string from uri
   * @param uriInput address for data input
   * @return response from client
   */
  def getHTMLBody(uriInput: String): F[Response[String]] = {

    implicit val backend: SttpBackend[F, Nothing] = AsyncHttpClientCatsBackend[F]()

    sttp.get(uri"$uriInput").send()

  }

  /**
   * Proceeding in parallel requests to site for multiple pages
   * @param config config for parsing
   * @param par implicit variable for Parallel instance
   * @return list with html body strings
   */
  def retrieveHTML(
    config: FoodSourceConfig
  )(implicit par: Parallel[F]): F[List[Response[String]]] = {

    val htmlList: ListBuffer[String] = ListBuffer.empty[String]

    (1 to config.pages).foreach(i => htmlList += s"${config.site}${config.sections.head}.html?p=$i")

    htmlList.toList.parTraverse(getHTMLBody)

  }

  /**
   * Retrieve data about products with help of jsoup
   * @param body string with html info
   * @return list of products info
   */
  def parseData(body: String): F[List[ProductParsed]] = {

    Blocker[F].use { blocker =>
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

}
