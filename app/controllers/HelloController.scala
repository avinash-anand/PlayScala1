package controllers

import play.api.mvc.{Action, Controller}

trait HelloController extends Controller {

  def hello = Action {
    implicit request =>
      Ok(views.html.hello(request))
  }

}

object HelloController extends HelloController
