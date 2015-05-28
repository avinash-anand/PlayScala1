package controllers

import models.Address
import org.jsoup.Jsoup
import org.mockito.Matchers
import org.mockito.Mockito.{reset, times, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.i18n.Messages
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import service.ReactiveMongoService

import scala.concurrent.Future

class AddressControllerSpec extends PlaySpec with OneServerPerSuite with MockitoSugar with BeforeAndAfterEach {

  trait TestAddressService extends ReactiveMongoService[Address] {
    override def collectionName: String = "address"

    override val formats = Address.formats
  }

  val mockAddressService: ReactiveMongoService[Address] = mock[TestAddressService]

  object TestAddressController extends AddressController {
    override val addressService = mockAddressService
  }

  override def beforeEach: Unit = {
    reset(mockAddressService)
  }

  "AddressController" must {

    "respond to /address-details" in {
      val result = route(FakeRequest(GET, s"/play-scala/address-details")).get
      status(result) must not be (NOT_FOUND)
    }

    "addressDetails" must {

      "respond with OK" in {
        val result = TestAddressController.addressDetails().apply(FakeRequest())
        status(result) must be(OK)
      }

      "must contain \"Hello World page\" as heading 1" in {
        val result = TestAddressController.addressDetails().apply(FakeRequest())
        val document = Jsoup.parse(contentAsString(result))
        document.title() must be(Messages("hello.title"))
        document.getElementById("header-1").text() must be("Hello World page")
      }

    }

    "submit" must {

      "form validation - for invalid data" must {

        "respond with bad request and respective errors on pages" in {
          val result = TestAddressController.submit.apply(FakeRequest())
          status(result) must be(BAD_REQUEST)
          val document = Jsoup.parse(contentAsString(result))
          document.select(".error").text() must include("This field is required")
        }

      }

      "form validation - for valid data" must {

        "respond with redirect for successful save in mongo" in {
          when(mockAddressService.create(Matchers.any())).thenReturn(Future.successful(Results.Status(CREATED)))
          val result = TestAddressController.submit.apply(FakeRequest()
            .withFormUrlEncodedBody("line1" -> "ABC", "line2" -> "line 2", "postcode" -> "110085", "country" -> "India"))
          status(result) must be(SEE_OTHER)
          redirectLocation(result).get must be("/play-scala/contact-details")
          verify(mockAddressService, times(1)).create(Matchers.any())
        }

        "respond with redirect to address-details page for invalid json in mongo" in {
          when(mockAddressService.create(Matchers.any())).thenReturn(Future.successful(Results.Status(BAD_REQUEST)))
          val result = TestAddressController.submit.apply(FakeRequest()
            .withFormUrlEncodedBody("line1" -> "ABC", "line2" -> "line 2", "postcode" -> "110085", "country" -> "India"))
          status(result) must be(SEE_OTHER)
          redirectLocation(result).get must be("/play-scala/address-details")
          verify(mockAddressService, times(1)).create(Matchers.any())
        }

        "respond with redirect to address-details page for any other status" in {
          when(mockAddressService.create(Matchers.any())).thenReturn(Future.successful(Results.Status(BAD_GATEWAY)))
          val result = TestAddressController.submit.apply(FakeRequest()
            .withFormUrlEncodedBody("line1" -> "ABC", "line2" -> "line 2", "postcode" -> "110085", "country" -> "India"))
          status(result) must be(SEE_OTHER)
          redirectLocation(result).get must be("/play-scala/address-details")
          verify(mockAddressService, times(1)).create(Matchers.any())
        }

      }

    }

  }

}
