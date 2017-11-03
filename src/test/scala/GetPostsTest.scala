/**
  * Created by Antoine Sauray on 03/11/2017.
  */

import java.text.{ParseException, SimpleDateFormat}

import org.scalatest.FunSuiteLike
import org.scalatra.test.scalatest._

import scala.util.parsing.json.JSON

trait GetPostsTest extends ScalatraSuite with FunSuiteLike {

  test("get posts") {
    // basic call and check for content
    get("/api/posts") {
      status should equal (200)
      // check the body
      val json = JSON.parseFull(body)
      assert(json.isDefined)
      val map = json.get.asInstanceOf[Map[String, Any]]
      assert(map.get("posts").isDefined)
      map("posts").asInstanceOf[List[Map[String,Any]]].foreach(fields => {
        assert(fields.get("id").isDefined && fields("id").isInstanceOf[Number])
        assert(fields.get("author").isDefined && fields("author").isInstanceOf[String])
        assert(fields.get("content").isDefined && fields("content").isInstanceOf[String])
        assert(fields.get("created_at").isDefined && fields("created_at").isInstanceOf[String])
        try {
          new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(fields("created_at").asInstanceOf[String])
        } catch {
          case _: ParseException => fail()
        }
      })
    }

    // call with author name
    get("/api/posts?author=Anonyme") {
      status should equal (200)
    }

    // call with from date
    get("/api/posts?from=2017-10-05T01:00:00") {
      status should equal (200)
    }

    // call with to date
    get("/api/posts?to=2017-11-01T01:00:00") {
      status should equal (200)
    }

    // call with author and from
    get("/api/posts?author=Anonyme&from=2017-10-05T01:00:00") {
      status should equal (200)
    }

    // call with author and to
    get("/api/posts?author=Anonyme&to=2017-10-05T01:00:00") {
      status should equal (200)
    }

    // call with from and to (from < to)
    get("/api/posts?from=2017-10-05T01:00:00&to=2017-10-06T01:00:00") {
      status should equal (200)
    }

    // call with from and to (from > to)
    get("/api/posts?from=2017-10-05T01:00:00&to=2017-10-01T01:00:00") {
      // return to the future: will return nothing but correct
      status should equal (200)
      val json = JSON.parseFull(body)
      assert(json.isDefined)
      val map = json.get.asInstanceOf[Map[String, Any]]
      assert(map.get("posts").isDefined)
      assert(map("posts").asInstanceOf[List[Map[String, Any]]].isEmpty)
    }

    // call with a weird from
    get("/api/posts?from=2017-1-05T01:00:00") {
      // the parser does work in this case
      status should equal (200)
    }

    // call with a wrong format
    get("/api/posts?from=2017-10-05-00T01:00:00") {
      status should equal (400)
    }

    // call with a weird to
    get("/api/posts?to=2017-1-05T01:00:00") {
      // the parser does work in this case
      status should equal (200)
    }

    // call with a wrong format
    get("/api/posts?to=2017-10-05-00T01:00:00") {
      // the parser will stop before the end but it will work
      status should equal (400)
    }

    // call with from and to, to being weird (from < to)
    get("/api/posts?from=2017-1-05T01:00:00&to=2017-11-01T01:00:00") {
      // the parser understands that 1 is 01
      status should equal (200)
    }

    // call with author, from and to, (from being weird) and (from < to)
    get("/api/posts?author=cendrillon&from=2017-1-05T01:00:00&to=2017-11-07T01:00:00") {
      // return to the future again (correct but will return nothing)
      status should equal (200)
      val json = JSON.parseFull(body)
      assert(json.isDefined)
      val map = json.get.asInstanceOf[Map[String, Any]]
      assert(map.get("posts").isDefined)
      assert(map("posts").asInstanceOf[List[Map[String, Any]]].isEmpty)
    }

    // call with author, from and to (to being weird) and from < to
    get("/api/posts?author=cendrillon&from=2016-11-01T01:00:00&to=2017-1-05T01:00:00") {
      // return to the future again (correct but will return nothing)
      status should equal (200)
    }

    // call with author, giving a int instead of string
    get("/api/posts?author=12") {
      // send a bunch of garbage
      status should equal (200)
    }

    // call with author, giving special chars
    get("/api/posts?author=éâàè") {
      // send a bunch of garbage
      status should equal (200)
    }

    // call with from, giving special chars
    get("/api/posts?from=éâàè") {
      // send a bunch of garbage
      status should equal (400)
    }

    // call with to, giving special chars
    get("/api/posts?to=éâàè") {
      // send a bunch of garbage
      status should equal (400)
    }

    // call with from, from not correct (yyyy-MM-dd)
    get("/api/posts?from=yesterday") {
      // send a bunch of garbage
      status should equal (400)
    }

    // call with to, to not correct (yyyy-MM-dd)
    get("/api/posts?to=tomorrow") {
      // send a bunch of garbage
      status should equal (400)
    }

    // call with from and to, from not correct (yyyy-MM-dd)
    get("/api/posts?from=yesterday&to=2017-10-10") {
      status should equal (400)
    }

    // call with from and to, to not correct (yyyy-MM-dd)
    get("/api/posts?from=2017-10-10&to=tomorrow") {
      status should equal (400)
    }

  }
}
