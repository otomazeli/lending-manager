package com.github.twinra.lendmanager.rest

import com.github.twinra.lendmanager.domain.Item
import com.github.twinra.lendmanager.repo.ItemRepository
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

class ItemsServlet(implicit val repo: ItemRepository) extends ScalatraServlet with LazyLogging with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    repo.readAll()
  }

  get("/:id") {
    repo.read(params("id").toLong).getOrElse(halt(404))
  }

  post("/") {
    val item = parsedBody.extract[Item]
    repo.add(item)
  }

  put("/:id") {
    val item = parsedBody.extract[Item]
    repo.update(params("id").toLong, item)
  }

  delete("/:id") {
    repo.delete(params("id").toLong)
  }
}
