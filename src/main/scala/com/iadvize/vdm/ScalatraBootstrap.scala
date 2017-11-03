package com.iadvize.vdm

/**
  * Created by Antoine Sauray on 02/11/2017.
  */

import javax.servlet.ServletContext

import com.iadvize.vdm.model.Posts
import org.scalatra._
import slick.jdbc.PostgresProfile
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

/**
  * Created by Antoine Sauray on 02/11/2017.
  * Lifecycle for the Servlet
  * Used for initializing the database
  */
class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new VDMSwagger

  var db: PostgresProfile.backend.DatabaseDef = _

  override def init(context: ServletContext) {
    try{
      db = Database.forConfig("docker")
      val posts = TableQuery[Posts]
      val schema = posts.schema
      db.run(DBIO.seq(
        //schema.drop, // uncomment to drop the database
        schema.create
      ))
      println("Starting server")
      context.mount(new VDMController(db, posts, swagger), "/api", "api")
      context.mount (new ResourcesApp, "/swagger", "swagger")
    } catch {
      case e:Exception => e.printStackTrace()
    }
  }

  override def destroy(context: ServletContext): Unit = {
    db.close()
  }
}
