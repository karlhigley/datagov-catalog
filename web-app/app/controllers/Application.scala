package controllers

import scala.collection.JavaConversions._

import play.api._
import play.api.mvc._
import play.api.libs.json._

import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.search.sort.SortOrder

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._

import models.Dataset

object Application extends Controller {
  val uri = ElasticsearchClientUri("elasticsearch://elasticsearch:9300")
  val settings = ImmutableSettings.settingsBuilder().put("cluster.name", "datagov_catalog").build()
  val client = ElasticClient.remote(settings, uri)

  def index = Action {
    Ok(views.html.Index())
  }

  def results = Action { request =>
    val searchString = request.queryString("q").mkString(" ")
    val elasticQuery = search in "datasets" query searchString sort (by field "metadata_created" order SortOrder.DESC) limit 10
    val response = client.execute(elasticQuery).await
    val datasets = response.hitsAs[Dataset]
    Ok(Json.toJson(datasets))
  }
}
