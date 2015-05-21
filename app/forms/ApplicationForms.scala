package forms

import models.Address
import play.api.data.Form
import play.api.data.Forms._

object ApplicationForms {

  val addressForm: Form[Address] = Form(mapping(
  "line1" -> nonEmptyText.verifying("Address line 1 is mandatory", line1 => line1.length > 0),
  "line2" -> nonEmptyText,
  "line3" -> optional(nonEmptyText),
  "line4" -> optional(nonEmptyText),
  "postcode" -> nonEmptyText,
  "country" -> nonEmptyText
  )(Address.apply)(Address.unapply))

}
