package dao


import scala.slick.lifted.ProvenShape
import java.sql.Date

import scala.slick.dao.{DBConnection, CRUDable, AbstractDAO}
import DBConnection.profile.simple._
import scala.slick.dao.{CRUDable, AbstractDAO}


case class Employee(name: String, email: String, note: Option[String], id: Long = 0)


class Employees(tag: Tag) extends Table[Employee](tag, "employee") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.NotNull)

  def email = column[String]("email", O.NotNull)

  def note = column[Option[String]]("note", O.Nullable)

  override def * = (name, email, note, id) <>(Employee.tupled, Employee.unapply)

}

trait IEmployeeDao extends AbstractDAO with CRUDable[Employees, Long] {

  val entities: TableQuery[Employees] = TableQuery[Employees]

  def selectBy(entity: Employee) = {
    for (e <- entities if e.id === entity.id) yield e
  }

  def selectById(id: Long) = {
    for (e <- entities if e.id === id) yield e
  }

}

class EmployeeDao(implicit innerSession: Session) extends IEmployeeDao


