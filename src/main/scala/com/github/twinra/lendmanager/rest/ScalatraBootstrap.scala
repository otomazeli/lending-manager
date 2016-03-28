package com.github.twinra.lendmanager.rest

import javax.servlet.ServletContext

import com.github.twinra.lendmanager.repo.DummyStorage
import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    implicit val repo = new DummyStorage{ init() }
    context.mount(new PeopleServlet, "/people/*")
    context.mount(new ItemsServlet, "/items/*")
    context.mount(new LendingsServlet, "/borrowings/*")
  }
}
