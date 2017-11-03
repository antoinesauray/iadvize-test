/**
  * Created by Antoine Sauray on 03/11/2017.
  */

import org.scalatest.FunSuiteLike
import org.scalatra.test.scalatest._

trait GetIndex extends ScalatraSuite with FunSuiteLike {

  test("get index") {
    get("/api") {
      status should equal (200)
    }
  }
}
