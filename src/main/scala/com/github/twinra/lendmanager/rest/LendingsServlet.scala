package com.github.twinra.lendmanager.rest

import java.time.LocalDate

import com.github.twinra.lendmanager.dao.LendingRepository
import com.github.twinra.lendmanager.domain.Lending
import com.github.twinra.lendmanager.repo.{ItemRepository, PersonRepository}
import com.github.twinra.utils.json.JLocalDateSerializer
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

import scala.util.{Failure, Success, Try}

private case class LendingMessage(itemId: Long, personId: Long, date: LocalDate)

class LendingsServlet(implicit repo: LendingRepository,
                      itemsRepo: ItemRepository,
                      peopleRepo: PersonRepository) extends ScalatraServlet with LazyLogging with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats + JLocalDateSerializer

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
    val lendingRequest = Try(parsedBody.extract[LendingMessage]) match {
      case Success(b) => b
      case Failure(ex) =>
        log("Failed extract lending", ex)
        halt(400)
    }
    val item = itemsRepo.read(lendingRequest.itemId).getOrElse(halt(404))
    val person = peopleRepo.read(lendingRequest.personId).getOrElse(halt(404))
    repo.add(Lending(item, person, lendingRequest.date))
  }

  delete("/:id") {
    repo.delete(params("id").toLong)
  }
}
