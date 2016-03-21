package com.github.twinra.lendmanager

import com.github.twinra.lendmanager.rest.ScalatraBootstrap
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener


object JettyLauncher extends LazyLogging {
  def main(args: Array[String]): Unit = {
    val cfg = ConfigFactory.load("application.conf")
    val server = new Server(cfg.getInt("cfg.http.port"))

    val handler = new WebAppContext("src/main/webapp", "/") {
      addEventListener(new ScalatraListener)
      setInitParameter(ScalatraListener.LifeCycleKey, classOf[ScalatraBootstrap].getCanonicalName)
      addServlet(classOf[DefaultServlet], "/")
    }

    server.setHandler(handler)
    server.start()
    server.join()
  }
}