package controllers

import forms.ApplicationForms.addressForm
import models.Address
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import service.{AddressService, ReactiveMongoService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AddressController extends Controller {

  val addressService: ReactiveMongoService[Address]

  def addressDetails: Action[AnyContent] = Action.async {
    implicit request =>
      addressService.fetchAll map {
        addressList =>
          Ok(views.html.addressDetails(addressForm, addressList))
      }
  }

  def submit: Action[AnyContent] = Action.async {
    implicit request =>
      addressForm.bindFromRequest.fold(
        formWithErrors => {
          addressService.fetchAll map {
            addressList =>
              BadRequest(views.html.addressDetails(formWithErrors, addressList))
          }
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
