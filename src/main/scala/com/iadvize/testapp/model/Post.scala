package com.iadvize.testapp.model

import com.github.tototoshi.slick.SQLiteJodaSupport._
import org.joda.time.DateTime
import slick.jdbc.H2Profile.api._



case class Post(id: Int, author: String, content: String, created_at: DateTime) {}
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
  def createdAt = column[DateTime]("created_at")

  def * = (id, author, content, createdAt) <> (Post.tupled, Post.unapply)

}

