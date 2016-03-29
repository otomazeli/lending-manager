package com.github.twinra.lendmanager.rest

import javax.servlet.ServletContext

import com.github.twinra.lendmanager.dao.LendingRepository
import com.github.twinra.lendmanager.repo.{ItemRepository, PersonRepository}
import org.scalatra.LifeCycle
import scalikejdbc.config.DBs

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    DBs.setup()
    implicit val itemsRepo = new ItemRepository {
      init()
    }
    implicit val peopleRepo = new PersonRepository {
      init()
    }
    implicit val lendingsRepo = new LendingRepository {
      init()
    }

    context.mount(new PeopleServlet, "/people/*")
    context.mount(new ItemsServlet, "/items/*")
    context.mount(new LendingsServlet, "/lendings/*")
  }

  override def destroy(context: ServletContext): Unit = DBs.closeAll()
}
