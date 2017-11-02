package com.iadvize.testapp.model

import java.text.SimpleDateFormat

import slick.jdbc.H2Profile.api._

case class Post(id: Int, author: String, content: String, datetime: String) {
  val date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(datetime)
}

/**
  * Created by Antoine Sauray on 02/11/2017.
  * content the content of the post
  * dateTime the date and time the post was created
  * author the author of the post
  */
class Posts(tag: Tag) extends Table[Post](tag, "posts") {
  def id = column[Int]("id", O.PrimaryKey)
  def author = column[String]("author")
  def content = column[String]("content")
  def datetime = column[String]("datetime")

  def * = (id, author, content, datetime) <> (Post.tupled, Post.unapply)
}

