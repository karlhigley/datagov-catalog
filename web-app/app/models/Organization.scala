package models

import play.api.libs.json._

case class Organization(
  title: String
)

object Organization {
  implicit val organizationWrites = new Writes[Organization] {
    def writes(organization: Organization) = Json.obj(
      "title" -> organization.title
    )
  }
}