import dao._
import scala.slick.dao.DBConnection

/**
 * User: Dimitr
 * Date: 08.03.14
 * Time: 13:57
 */
object DaoFactory {
  private implicit def session = DBConnection.databasePool.createSession()

  def getEmployeeDao: IEmployeeDao = new EmployeeDao()

}
