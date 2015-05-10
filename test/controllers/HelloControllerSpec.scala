package controllers

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._

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
        contentAsString(result) must include("Hello World page")
      }

    }

  }

}
