
import com.iadvize.vdm.{VDMController, VDMSwagger}
import com.iadvize.vdm.model.Posts
import org.scalatest.BeforeAndAfterAll
import org.scalatra.test.scalatest.ScalatraSuite
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery


/**
  * Created by Antoine Sauray on 03/11/2017.
  */
class MasterSuite extends ScalatraSuite with BeforeAndAfterAll with GetIndex with GetPostTest with GetPostsTest {
  implicit val swagger = new VDMSwagger

  override def beforeAll(): Unit = {
    super.beforeAll()
    // setup the servlet for testing purpose
    val db = Database.forURL("jdbc:postgresql://127.0.0.1:5432/vdm?user=vdm&password=vdm", driver = "org.postgresql.Driver")
    val vdmServlet = new VDMController(db, TableQuery[Posts], swagger)
    addServlet(vdmServlet, "/api", "api")
  }
}

