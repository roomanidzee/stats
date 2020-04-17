package com.romanidze.foodstats.modules.hello.routes

import cats.effect.IO
import org.http4s.syntax.kleisli._
import org.http4s.{Method, Request, Status, Uri}
import org.scalatest.{Matchers, WordSpec}

class HelloRoutesSpec extends WordSpec with Matchers {

  "Request to Hello endpoint" should {

      "retrieve simple message" in {

        val request         = Request[IO](Method.GET, Uri.uri("/hello"))
        val expectedMessage = "Hello, food statistics user!"

        val responseIO = new HelloRoutes[IO]().routes.orNotFound(request).unsafeRunSync()

        responseIO.status shouldBe Status.Ok

        responseIO.as[String].unsafeRunSync() shouldBe expectedMessage

      }

    }

}
