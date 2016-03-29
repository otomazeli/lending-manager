package com.github.twinra.lendmanager.repo

import com.github.twinra.lendmanager.domain.Item
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import scalikejdbc.config.DBsWithEnv

class ItemRepositoryTest extends FlatSpec with Matchers with BeforeAndAfter {
  lazy val repo = new ItemRepository
  before {
    DBsWithEnv("test").setup()
    repo.destroy()
    repo.init()
  }
  after {
    repo.destroy()
    DBsWithEnv("test").closeAll()
  }

  it should "handle CRUD operations correctly" in {
    //precondition
    repo.readAll() should have size 0

    //when
    val id = repo.add(Item("Book"))
    //then
    repo.read(id) should not be None
    repo.read(id).get should be(Item("Book", Some(id)))
    repo.readAll() should have size 1

    //when
    repo.update(id, Item("Textbook"))
    repo.read(id).get should be(Item("Textbook", Some(id)))
    repo.readAll() should have size 1
    //then
    repo.delete(id)
    repo.readAll() should have size 0
  }

}
