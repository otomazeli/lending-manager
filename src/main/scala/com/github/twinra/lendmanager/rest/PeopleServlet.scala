package com.github.twinra.lendmanager.rest

import com.github.twinra.lendmanager.domain.Person
import com.github.twinra.lendmanager.repo.PersonRepository
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json._

class PeopleServlet(implicit val repo: PersonRepository) extends ScalatraServlet with LazyLogging with JacksonJsonSupport {
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
    val person = parsedBody.extract[Person]
    repo.add(person)
  }

  put("/:id") {
    val person = parsedBody.extract[Person]
    repo.update(params("id").toLong, person)
  }

  delete("/:id") {
    repo.delete(params("id").toLong)
  }
}
