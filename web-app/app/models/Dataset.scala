package models

import play.api.libs.json._

case class Dataset(
  title: String,
  notes: String,
  organization: Organization
)

object Dataset {
  implicit val datasetWrites = new Writes[Dataset] {
    def writes(dataset: Dataset) = Json.obj(
      "title" -> dataset.title,
      "notes" -> dataset.notes,
      "organization" -> dataset.organization
    )
  }
}