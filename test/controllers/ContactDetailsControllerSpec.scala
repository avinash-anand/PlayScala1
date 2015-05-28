package controllers

import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ContactDetailsControllerSpec extends PlaySpec with OneServerPerSuite {

  object TestContactDetailsController extends ContactDetailsController

  "ContactDetailsController" must {

    "respond to /contact-details" in {
      val result = route(FakeRequest(GET, "/play-scala/contact-details")).get
      status(result) must not be (NOT_FOUND)
    }

    "contactDetails" must {
      "respond with OK" in {
        val result = TestContactDetailsController.contactDetails.apply(FakeRequest())
        status(result) must be(OK)
      }
    }

    "submit" must {
      "respond with OK" in {
        val result = TestContactDetailsController.submit.apply(FakeRequest())
        status(result) must be(OK)
      }
    }
  }

}
