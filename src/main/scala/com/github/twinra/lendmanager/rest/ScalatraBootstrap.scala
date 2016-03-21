package com.github.twinra.lendmanager.rest

import javax.servlet.ServletContext

import com.github.twinra.lendmanager.repo.DummyRepository
import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    implicit val repo = new DummyRepository{ init() }
    context.mount(new PeopleServlet, "/people/*")
    context.mount(new ItemsServlet, "/items/*")
    context.mount(new LendingsServlet, "/borrowings/*")
  }
}
