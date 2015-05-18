package models

import play.api.libs.json.Json

case class Address( line1: String, line2: String, line3: Option[String], line4: Option[String], postcode: String, country: String )

object Address {
  implicit val formats = Json.format[Address]
}
