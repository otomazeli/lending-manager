package com.github.twinra.lendmanager.repo

import java.time.LocalDate

import com.github.twinra.lendmanager.dao.LendingRepository
import com.github.twinra.lendmanager.domain.{Item, Lending, Person}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import scalikejdbc.config.DBsWithEnv

class LendingRepositoryTest extends FlatSpec with Matchers with BeforeAndAfter {
  lazy val itemRepo = new ItemRepository
  lazy val personRepo = new PersonRepository
  lazy val lendingRepo = new LendingRepository

  before {
    DBsWithEnv("test").setup()
    Seq(personRepo, itemRepo, lendingRepo).foreach { repo =>
      repo.destroy()
      repo.init()
    }
  }

  after {
    Seq(personRepo, itemRepo, lendingRepo).foreach(_.destroy())
    DBsWithEnv("test").closeAll()
  }

  it should "handle CRUD operations with joins properly" in {
    Seq(itemRepo, personRepo, lendingRepo).foreach { repo =>
      repo.readAll() should have size 0
    }

    val date = LocalDate.now()
    val itemName = "Book"
    val personName = "John"

    val (personId, itemId) = (personRepo.add(Person("John")), itemRepo.add(Item("Book")))

    val (person, item) = (personRepo.read(personId), itemRepo.read(itemId))
    person.nonEmpty should be(true)
    item.nonEmpty should be(true)

    person.get.name should be(personName)
    item.get.name should be(itemName)

    val lendingId = lendingRepo.add(Lending(item.get, person.get, date))

    val lending = lendingRepo.read(lendingId)
    lending.nonEmpty should be(true)
    lending.get.item.name should be(itemName)
    lending.get.person.name should be(personName)
    lending.get.date should be(date)

    Seq(itemRepo, personRepo, lendingRepo).foreach { repo =>
      repo.readAll() should have size 1
    }
  }
}
