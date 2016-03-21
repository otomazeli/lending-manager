package com.github.twinra.lendmanager.rest

import com.github.twinra.lendmanager.domain.Item
import com.github.twinra.lendmanager.repo.Repository
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

class ItemsServlet(implicit val repo: Repository) extends ScalatraServlet with LazyLogging with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    repo.items
  }

  get("/:id") {
    repo.items.find(_.id.get == params("id").toLong).orElse(halt(404))
  }

  post("/") {
    val item = parsedBody.extract[Item]
    repo.addItem(item.copy(id = None))
  }

  put("/:id") {
    ??? //TODO
  }

  delete("/:id") {
    ??? //TODO
  }
}
