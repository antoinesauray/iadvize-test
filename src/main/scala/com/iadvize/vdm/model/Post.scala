package com.iadvize.vdm.model

import java.sql.Timestamp

import com.iadvize.vdm.utils.MyPostgresProfile.api._

/**
  * Created by Antoine Sauray on 02/11/2017.
  * content the content of the post
  * dateTime the date and time the post was created
  * author the author of the post
  */
case class Post(id: Int, content: String, date: Timestamp, author: String) {}

/**
  * Posts database
  * @param tag
  */
class Posts(tag: Tag) extends Table[Post](tag, "posts") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def content = column[String]("content")
  def date = column[Timestamp]("date")
  def author = column[String]("author")

  def * = (id, content, date, author) <> (Post.tupled, Post.unapply)

}