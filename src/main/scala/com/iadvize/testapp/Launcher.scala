package com.iadvize.testapp

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
/**
  * Created by antoinesauray on 02/11/2017.
  */
object Launcher { // this is my entry object as specified in sbt project definition
  def main(args: Array[String]) {
    val port = if(System.getenv("PORT") != null) System.getenv("PORT").toInt else 8080
    println("Listening on port " + port)

    val server = new Server(port)
    val context = new WebAppContext()
    context setContextPath "/"
    context.setResourceBase("src/main/scala")
    context.setInitParameter(ScalatraListener.LifeCycleKey, "com.iadvize.testapp.ScalatraBootstrap")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")

    server.setHandler(context)
    server.start()
    server.join()

  }
}