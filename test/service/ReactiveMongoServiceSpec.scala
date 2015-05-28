package service

import models.{Address, Contact}
import org.mockito.Matchers
import org.mockito.Mockito.{reset, times, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.core.commands.LastError

import scala.concurrent.Future

class ReactiveMongoServiceSpec extends PlaySpec with OneServerPerSuite with MockitoSugar with BeforeAndAfterEach {

  val mockCollection = mock[JSONCollection]

  case class TestData(name: String)

  object TestData {
    implicit val formats = Json.format[TestData]
  }

  object TestReactiveMongoService extends ReactiveMongoService[TestData] {
    override def collectionName: String = "test"

    override val formats = TestData.formats

    override def collection: JSONCollection = mockCollection
  }

  override def beforeEach: Unit = {
    reset(mockCollection)
  }

  "ReactiveMongoService" must {

    "implement correct ReactiveMongoCollection" in {

    }

    "create" must {
      "for valid JSON, return status CREATED(201)" in {
        val inputJson = Json.parse( """{"name":"Avinash"}""")
        when(mockCollection.insert(Matchers.eq(TestData("Avinash")), Matchers.any())
          (Matchers.any(), Matchers.any()))
          .thenReturn(Future.successful(LastError(true, None, None, None, None, 0, false)))
        val result = TestReactiveMongoService.create(inputJson)
        status(result) must be(CREATED)
        verify(mockCollection, times(1)).insert(Matchers.eq(TestData("Avinash")), Matchers.any())(Matchers.any(), Matchers.any())
      }
      "for invalid Json, return status BAR_REQUEST" in {
        val inputJson = Json.parse( """{"name":1234}""")
        val result = TestReactiveMongoService.create(inputJson)
        status(result) must be(BAD_REQUEST)
        verify(mockCollection, times(0)).insert(Matchers.eq(TestData("Avinash")), Matchers.any())(Matchers.any(), Matchers.any())
      }
    }
  }

  "AddressService" must {
    "override correct collection name" in {
      AddressService.collectionName must be("address")
    }
    "override correct formats" in {
      AddressService.formats must be(Address.formats)
    }
    "override correct collection" in {
      AddressService.collection must be(ReactiveMongoPlugin.db.collection[JSONCollection](AddressService.collectionName))
    }

  }

  "ContactService" must {
    "override correct collection name" in {
      ContactService.collectionName must be("contact")
    }
    "override correct formats" in {
      ContactService.formats must be(Contact.formats)
    }
    "override correct collection" in {
      ContactService.collection must be(ReactiveMongoPlugin.db.collection[JSONCollection](ContactService.collectionName))
    }

  }

}
