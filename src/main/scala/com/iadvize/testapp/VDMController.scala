package com.iadvize.testapp

import com.github.tototoshi.slick.SQLiteJodaSupport._
import com.iadvize.testapp.model.{Post, Posts}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}
import org.json4s.JsonDSL._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger._
import org.scalatra.{BadRequest, Ok, ScalatraServlet}
import play.api.libs.json._
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by Antoine Sauray on 02/11/2017.
  */
class VDMController(db: Database, posts: TableQuery[Posts]) extends ScalatraServlet with NativeJsonSupport with SwaggerSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    // set the format to json for every route
    contentType = formats("json")
  }

  /**
    * Specification for getPosts route
    */
  val getPosts: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[List[Post]]("getPosts")
      summary "Show all posts"
      notes "Shows all the posts available. You can search it too."
      parameters(
      Parameter("from", DataType.Date, paramType = ParamType.Query, required = false),
      Parameter("to", DataType.Date, paramType = ParamType.Query, required = false),
      Parameter("author", DataType.String, paramType = ParamType.Query, required = false)
    ))

  /**
    * Specification for getPost route
    */
  val getPost: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[Post]("getPost")
      summary "Show a single post"
      notes "Show a single post based on its identifier"
      parameter Parameter("id", DataType.String, paramType = ParamType.Path, required = true)
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
    val id = params.getAs[Int]("id")
    if(id.isDefined) {
      val q = for(c <- posts if c.id === id.get) yield c; Ok("post" -> Await.result(db.run(q.result), Duration("5s")))
    } else BadRequest
  }

  /**
    * Retrieve a list of posts
    */
  get("/posts", operation(getPosts)) {
    // get parameters

    val from = params.getAs[String]("from")
    val to = params.getAs[String]("to")
    val author = params.getAs[String]("author")

    // prepare date formatter
    val simpleDateISOFormat = DateTimeFormat.forPattern("yyyy-MM-dd")
    // prepare list of conditions that will need to be satisfied in the SQL query
    var conditions = List[Posts => Rep[Boolean]]()

    // if author is defined, we add a condition to check the author
    if(author.isDefined) {
      conditions = ((a: Posts) => a.author === author.get) :: conditions
    }

    // if from is defined, we add a condition to check the creation date
    if(from.isDefined) {
      val date = simpleDateISOFormat.withZone(DateTimeZone.UTC).parseDateTime(from.get)
      conditions = ((a: Posts) => a.createdAt >= date) :: conditions
    }

    // defining the validate function which will validate all the conditions
    def validate(p: Posts, conditions: List[Posts => Rep[Boolean]]): Rep[Boolean]= {
      if (conditions.tail.isEmpty) conditions.head(p)
      else conditions.head(p) && validate(p, conditions.tail)
    }

    // pre initialize the query variable
    var q: slick.lifted.Query[Posts, Post, Seq] = null

    // if there are no conditions we execute the query without checking
    if(conditions.isEmpty) {
      q = for(p <- posts) yield p
    } else {
      // otherwise we will validate them
      q = for(p <- posts if validate(p, conditions)) yield p
    }

    // we get the results
    val results = Await.result(db.run(q.result), Duration("5s"))

    // This is the Json formatter that will be used
    implicit val fmt: Format[Post] = new Format[Post] {
      def reads(js: JsValue): JsResult[Post] = JsSuccess(Post(0, "", "", DateTime.now()))
      def writes(t: Post): JsValue = {
        val dateTimeFormat: DateTimeFormatter = ISODateTimeFormat.dateTime()
        Json.obj("id" -> t.id, "author" -> t.author, "content" -> t.content, "created_at" -> t.created_at.toString(dateTimeFormat))
      }
    }
    // deliver json with OK (200) http code
    Ok("posts" -> Json.toJson(results))
  }

  override protected implicit def swagger: SwaggerEngine[_] = new VDMSwagger

  override protected def applicationDescription: String = "The VDM API. It exposes operations for browsing VDM posts as well as retrieving a single one"
}
