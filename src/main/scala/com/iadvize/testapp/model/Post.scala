package com.iadvize.testapp.model

import java.sql.Timestamp

import com.iadvize.testapp.utils.MyPostgresProfile.api._


case class Post(id: Int, author: String, content: String, created_at: Timestamp) {}
/**
  * Created by Antoine Sauray on 02/11/2017.
  * content the content of the post
  * dateTime the date and time the post was created
  * author the author of the post
  */
class Posts(tag: Tag) extends Table[Post](tag, "posts") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def author = column[String]("author")
  def content = column[String]("content")
  def createdAt = column[Timestamp]("created_at")

  def * = (id, author, content, createdAt) <> (Post.tupled, Post.unapply)

}