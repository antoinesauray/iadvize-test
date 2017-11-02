package com.iadvize.testapp.model

import slick.jdbc.H2Profile.api._

case class Post(id: Int, author: String, content: String, createdAt: String) {

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
  def createdAt = column[String]("created_at")

  def * = (id, author, content, createdAt) <> (Post.tupled, Post.unapply)

  /*
  def createTriggerDate: DBIO[Int] =
    sqlu"""CREATE TRIGGER check_date_format BEFORE INSERT,UPDATE on posts
          BEGIN
          SELECT
          CASE
          WHEN NEW.created_at NOT LIKE '%_@__%.__%' THEN
          RAISE (ABORT,'Invalid email address')
          END
      """
  */
}

