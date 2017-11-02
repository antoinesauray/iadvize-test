package com.iadvize.testapp

/**
  * Created by antoinesauray on 02/11/2017.
  */
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new VDMSwagger

  override def init(context: ServletContext) {
    context.mount(new VDMController, "/api", "api")
    context.mount (new ResourcesApp, "/swagger")
  }
}
