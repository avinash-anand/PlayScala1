package controllers

import play.api.mvc.{AnyContent, Action, Controller}

trait ContactDetailsController extends Controller {

  def contactDetails: Action[AnyContent] = Action {
    implicit request =>
      Ok(views.html.contactDetails(request))
  }

  def submit: Action[AnyContent] = Action {
    implicit request =>
      Ok
  }

}

object ContactDetailsController extends ContactDetailsController
