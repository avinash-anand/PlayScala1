package controllers

import play.api.mvc.{Action, Controller}

trait ContactDetailsController extends Controller {

  def contactDetails = Action {
    implicit request =>
      Ok
  }

  def submit = Action {
    implicit request =>
      Ok
  }

}

object ContactDetailsController extends ContactDetailsController
