package com.iadvize.testapp

/**
  * Created by Antoine Sauray on 02/11/2017.
  */

import javax.servlet.ServletContext

import com.iadvize.testapp.model.Posts
import org.scalatra._
import slick.jdbc.PostgresProfile
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._


class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new VDMSwagger
  var db: PostgresProfile.backend.DatabaseDef = _

  override def init(context: ServletContext) {
    db = Database.forURL("jdbc:postgresql://127.0.0.1:5432/vdm?user=vdm&password=vdm", driver = "org.postgresql.Driver")
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
