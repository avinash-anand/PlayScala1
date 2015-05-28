package service

import models.{Address, Contact}
import play.api.Logger
import play.api.Play.current
import play.api.libs.json.{Format, JsValue}
import play.api.mvc.{Result, Controller}
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ReactiveMongoService[T] extends Controller {

  def collectionName: String

  def collection: JSONCollection = ReactiveMongoPlugin.db.collection[JSONCollection](collectionName)

  implicit val formats: Format[T]

  val INVALID_JSON = "Invalid JSON"

  def create(data: JsValue): Future[Result] = {
    Logger.info("############## data = " + data)
    val isValid = data.validate[T].isSuccess
    Logger.info("############## isValid = " + isValid)
    isValid match {
      case true => {
        val dataToSave = data.as[T]
        collection.insert(dataToSave) map {
          lastError =>
            Logger.info("lastError.ok = " + lastError.ok)
            Logger.info("Successfully inserted with LastError: " + lastError)
            Created
        }
      }
      case false => {
        Future.successful(BadRequest(INVALID_JSON))
      }
    }
  }

  // TODO - define fetch method

  // TODO - define update method

}

object AddressService extends ReactiveMongoService[Address] {
  override def collectionName: String = "address"

  override val formats = Address.formats
}

object ContactService extends ReactiveMongoService[Contact] {
  override def collectionName: String = "contact"

  override val formats = Contact.formats
}
