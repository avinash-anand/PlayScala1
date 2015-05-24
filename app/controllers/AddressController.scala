package controllers

import java.util.UUID

import forms.ApplicationForms._
import models.Address
import play.api.libs.json.Json
import play.api.mvc.{Session, Action, Controller}
import service.{ReactiveMongoService, AddressService}

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

trait AddressController extends Controller {

  val addressService: ReactiveMongoService[Address]

  def addressDetails = Action {
    implicit request =>
      Ok(views.html.addressDetails(addressForm)).withSession(Session(Map("session-id" -> s"session-${UUID.randomUUID()}")))
  }

  def submit = Action.async {
    implicit request =>
      addressForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.addressDetails(formWithErrors)))
      },
      formData => {
        addressService.create(Json.toJson(formData)) flatMap {
          result => result.header.status match {
            case CREATED => Future.successful(Redirect(controllers.routes.ContactDetailsController.contactDetails))
            case BAD_REQUEST => Future.successful(Redirect(controllers.routes.AddressController.addressDetails))
            case _ => Future.successful(Redirect(controllers.routes.AddressController.addressDetails))
          }
        }
      }
      )
  }

}

object AddressController extends AddressController {
  val addressService: ReactiveMongoService[Address] = AddressService
}
