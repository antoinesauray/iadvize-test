package com.iadvize.testapp

import com.iadvize.testapp.model.Post
import org.json4s.JsonDSL._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.scalatra.swagger._
import org.scalatra.{NotImplemented, Ok, ScalatraServlet}

/**
  * Created by Antoine Sauray on 02/11/2017.
  */
class VDMController extends ScalatraServlet with NativeJsonSupport with SwaggerSupport {

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
    * Retrieve a list of posts
    */
  get("/posts", operation(getPosts)) {
    NotImplemented()
  }

  /**
    * Retrieve a single post by id
    */
  get("/post/:id", operation(getPost)){
    NotImplemented()
  }

  override protected implicit def swagger: SwaggerEngine[_] = new VDMSwagger

  override protected def applicationDescription: String = "The VDM API. It exposes operations for browsing VDM posts as well as retrieving a single one"
}
