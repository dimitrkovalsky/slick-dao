slick-dao
=========

Simple slick DAO support


For DAO support you need extend AbstractDAO with CRUDable
CRUDable[T,K] is Generic type. For example : 

Create simple entity for storing in common way for Slick

case class Employee(name: String, email: String, note: Option[String], id: Long = 0)


class Employees(tag: Tag) extends Table[Employee](tag, "employee") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.NotNull)

  def email = column[String]("email", O.NotNull)

  def note = column[Option[String]]("note", O.Nullable)

  override def * = (name, email, note, id) <>(Employee.tupled, Employee.unapply)

}


And than extend AbstractDAO with CRUDable, where K is type of primary key.


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

You need to implement 2 methods selectBy and selectById for select necessary entity for dao operations

After that you can create DAO factory to use implecit session support

object DaoFactory {
  private implicit def session = DBConnection.databasePool.createSession()

  def getEmployeeDao: IEmployeeDao = new EmployeeDao()

}

And than you can use common DAO operation in simple way : 

 val dao = DaoFactory.getEmployeeDao

 dao.insert(Employee("Simple", "simple@mail.com", Some("Some note")))

 for (obj <- dao.findPage(1, 10))
   println(obj)
