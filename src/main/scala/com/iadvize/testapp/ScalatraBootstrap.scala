package com.iadvize.testapp

/**
  * Created by Antoine Sauray on 02/11/2017.
  */

import javax.servlet.ServletContext

import com.iadvize.testapp.model.Posts
import org.scalatra._
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery


class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new VDMSwagger
  val db = Database.forURL("jdbc:sqlite:/Users/antoinesauray/Projects/Scala/test-backend-iadvize/vdm.db", driver = "org.sqlite.JDBC")

  override def init(context: ServletContext) {
    val posts = TableQuery[Posts]
    val schema = posts.schema
    db.run(DBIO.seq(
      //schema.drop, // uncomment to drop the database
      schema.create
    ))

    println("Starting server")
    context.mount(new VDMController(db, posts), "/api", "api")
    context.mount (new ResourcesApp, "/swagger")
  }

  override def destroy(context: ServletContext): Unit = {
    db.close()
  }
}
