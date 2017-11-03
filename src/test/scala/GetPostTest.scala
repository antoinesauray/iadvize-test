/**
  * Created by Antoine Sauray on 03/11/2017.
  */

import java.text.{ParseException, SimpleDateFormat}
import java.util.Date

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

import scala.util.Try
import scala.util.parsing.json.JSON

trait GetPostTest extends ScalatraSuite with FunSuiteLike {

  test("get a single post") {
    get("/api/posts/1") {
      val json = JSON.parseFull(body)
      assert(json.isDefined)
      val map = json.get.asInstanceOf[Map[String, Any]]
      assert(map.get("post").isDefined)
      if(status == 200) {
        // we need to check the json
        val fields = map("post").asInstanceOf[Map[String,Any]]
        assert(fields.get("id").isDefined && fields("id").isInstanceOf[Number])
        assert(fields.get("author").isDefined && fields("author").isInstanceOf[String])
        assert(fields.get("content").isDefined && fields("content").isInstanceOf[String])
        assert(fields.get("created_at").isDefined && fields("created_at").isInstanceOf[String])
        try {
          new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(fields("created_at").asInstanceOf[String])
        } catch {
          case _: ParseException => fail()
        }
      }
      // otherwise you need to fill the database
    }

    // call with id being a string
    get("/api/posts/bla") {
      status should equal (400)
    }

    // call with id being a string
    get("/api/posts/2a") {
      status should equal (400)
    }

    // call with id being special characters
    get("/api/posts/é$aàè") {
      status should equal (400)
    }
  }
}
