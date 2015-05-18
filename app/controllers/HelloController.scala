package controllers

import forms.ApplicationForms._
import play.api.mvc.{Action, Controller}

trait HelloController extends Controller {

  def hello = Action {
    implicit request =>
      Ok(views.html.hello(addressForm))
  }

  def submit = Action {
    implicit request =>
      addressForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.hello(formWithErrors))
      },
      formData => {
        Ok
      }
      )
  }

}

object HelloController extends HelloController
