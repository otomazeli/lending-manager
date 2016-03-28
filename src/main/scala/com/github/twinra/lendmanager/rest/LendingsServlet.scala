package com.github.twinra.lendmanager.rest

import java.time.LocalDate

import com.github.twinra.lendmanager.repo.Storage
import com.github.twinra.utils.json.JLocalDateSerializer
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

import scala.util.{Failure, Success, Try}

private case class LendingMessage(itemId: Long, personId: Long, date: LocalDate)
class LendingsServlet(implicit val repo: Storage) extends ScalatraServlet with LazyLogging with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats + JLocalDateSerializer

  before() {
    contentType = formats("json")
  }

  get("/") {
    repo.lendings
  }

  get("/:id") {
    repo.lendings.find(_.id.get == params("id").toLong).orElse(halt(404))
  }

  post("/") {
    val borrowRequest = Try(parsedBody.extract[LendingMessage]) match {
      case Success(b) => b
      case Failure(ex) =>
        log("Failed extract lending", ex)
        halt(400)
    }
    repo.lend(
      repo.personById(borrowRequest.personId).getOrElse(halt(404)),
      repo.itemById(borrowRequest.itemId).getOrElse(halt(404)),
      borrowRequest.date)
  }

  delete("/:id") {
    ??? //TODO
  }
}
