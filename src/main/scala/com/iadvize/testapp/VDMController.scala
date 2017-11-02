package com.iadvize.testapp

import java.text.SimpleDateFormat

import com.iadvize.testapp.model.{Post, Posts}
import org.json4s.JsonDSL._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.scalatra.swagger._
import org.scalatra.{BadRequest, NotImplemented, Ok, ScalatraServlet}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.macros.ParseException

/**
  * Created by Antoine Sauray on 02/11/2017.
  */
class VDMController(db: Database, posts: TableQuery[Posts]) extends ScalatraServlet with NativeJsonSupport with SwaggerSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  val getPosts: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[List[Post]]("getPosts")
      summary "Show all posts"
      notes "Shows all the posts available. You can search it too."
      parameters(
      Parameter("from", DataType.Date, paramType = ParamType.Query, required = false),
      Parameter("to", DataType.Date, paramType = ParamType.Query, required = false),
      Parameter("author", DataType.String, paramType = ParamType.Query, required = false)
    ))
  val getPost: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[Post]("getPost")
      summary "Show a single post"
      notes "Show a single post based on its identifier"
      parameter Parameter("id", DataType.String, paramType = ParamType.Path, required = true)
      )
  val getIndex: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[Post]("getIndex")
      summary "Show the index of the API"
      )

  /**
    * Retrieve a list of records
    */
  get("/") {
    Ok(
      ("message" -> "this is the backend for iAdvize") ~ ("version" -> "0.0.1")
    )
  }

  /**
    * Retrieve a single post by id
    */
  get("/posts/:id", operation(getPost)) {
    params.getAs[Int]("id") match {
      case id => val q = for(c <- posts if c.id === id.get) yield c; Ok("post" -> Await.result(db.run(q.result), Duration("5s")))
      case _ => BadRequest
    }
  }

  /**
    * Retrieve a list of posts
    */
  get("/posts", operation(getPosts)) {
    val fromStr = params.getAsOrElse[String]("from", null)
    val toStr = params.getAsOrElse[String]("to", null)
    val author = params.getAsOrElse[String]("author", null)
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
    var conditions = List[Posts => Rep[Boolean]]()

    params.getAs[String]("author") match {
      case author: Option[String] => println("adding condition on author"); val f = (a: Posts) => a.author === author.get; conditions = List[Posts => Rep[Boolean]](f)
    }

    def validate(p: Posts, conditions: List[Posts => Rep[Boolean]]): Rep[Boolean]= {
      if (conditions.tail.isEmpty) conditions.head(p)
      else conditions.head(p) && validate(p, conditions.tail)
    }
    val q = for(p <- posts if validate(p, conditions)) yield p; Ok("posts" -> Await.result(db.run(q.result), Duration("5s")))
  }



  override protected implicit def swagger: SwaggerEngine[_] = new VDMSwagger

  override protected def applicationDescription: String = "The VDM API. It exposes operations for browsing VDM posts as well as retrieving a single one"
}
