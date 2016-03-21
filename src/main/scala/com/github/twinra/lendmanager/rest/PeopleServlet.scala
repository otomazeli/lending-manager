package com.github.twinra.lendmanager.rest

import com.github.twinra.lendmanager.domain.Person
import com.github.twinra.lendmanager.repo.Repository
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{Formats, DefaultFormats}
import org.scalatra.ScalatraServlet
import org.scalatra.json._

class PeopleServlet(implicit val repo: Repository) extends ScalatraServlet with LazyLogging with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    repo.people
  }

  get("/:id") {
    repo.people.find(_.id.get == params("id").toLong).getOrElse(halt(404))
  }

  post("/") {
    val person = parsedBody.extract[Person]
    repo.addPerson(person.copy(id = None))
  }

  put("/:id") {
    ??? //TODO
  }

  delete("/:id") {
    ??? //TODO
  }
}
