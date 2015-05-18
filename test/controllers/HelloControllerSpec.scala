package controllers

import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.i18n.Messages
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.jsoup.Jsoup

class HelloControllerSpec extends PlaySpec with OneServerPerSuite {

  object TestHelloController extends HelloController

  "HelloController" must {

    "respond to /hello" in {
      val result = route(FakeRequest(GET, s"/play-scala/hello")).get
      status(result) must not be(NOT_FOUND)
    }

    "hello" must {

      "respond with OK" in {
        val result = TestHelloController.hello().apply(FakeRequest())
        status(result) must be(OK)
      }

      "must contain \"Hello World page\" as heading 1" in {
        val result = TestHelloController.hello().apply(FakeRequest())
        val document = Jsoup.parse(contentAsString(result))
        document.title() must be(Messages("hello.title"))
        document.getElementById("header-1").text() must be("Hello World page")
      }

    }

    "submit" must {

      "form validation - for invalid data" must {

        "respond with bad request and respective errors on pages" in {
          val result = TestHelloController.submit.apply(FakeRequest())
          status(result) must be(BAD_REQUEST)
          val document = Jsoup.parse(contentAsString(result))
          document.select(".error").text() must include("This field is required")
        }

      }

      "form validation - for valid data" must {

        "respond with OK" in {
          val result = TestHelloController.submit.apply(FakeRequest()
            .withFormUrlEncodedBody("line1" -> "ABC", "line2" -> "line 2", "postcode" -> "110085", "country" -> "India"))
          status(result) must be(OK)
        }

      }

    }

  }

}
