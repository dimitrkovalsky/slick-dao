package dao
import scala.slick.dao.DBConnection.profile.simple._
import scala.slick.dao._


/** Entity class storing rows of table Persons
 *  @param id Database column id AutoInc, PrimaryKey
 *  @param taxId Database column tax_id 
 *  @param fromDate Database column from_date 
 *  @param note Database column note  */
case class Person(id: Int, taxId: Option[Long], fromDate: Option[java.sql.Date], note: Option[String])
/** Table description of table person. Objects of this class serve as prototypes for rows in queries. */
class Persons(tag: Tag) extends Table[Person](tag, "person") {
  def * = (id, taxId, fromDate, note) <> (Person.tupled, Person.unapply)
  
  /** Database column id AutoInc, PrimaryKey */
  val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  /** Database column tax_id  */
  val taxId: Column[Option[Long]] = column[Option[Long]]("tax_id")
  /** Database column from_date  */
  val fromDate: Column[Option[java.sql.Date]] = column[Option[java.sql.Date]]("from_date")
  /** Database column note  */
  val note: Column[Option[String]] = column[Option[String]]("note")
}

trait IPersonDao extends AbstractDAO with CRUDable[Persons, Int]

class PersonDao(implicit innerSession: Session) extends IPersonDao {

  val entities: TableQuery[Persons] = TableQuery[Persons]

  def selectBy(entity: Person) = {
    for (e <- entities if e.id === entity.id) yield e
  }

  def selectById(id: Int) = {
    for (e <- entities if e.id === id) yield e
  }
}