package dao

import scala.slick.dao.DBConnection

object DaoFactory {
  private implicit def session = DBConnection.databasePool.createSession()

  def getEmployeeDao: IEmployeeDao = new EmployeeDao()

  def getPersonDao: IPersonDao = new PersonDao()
}