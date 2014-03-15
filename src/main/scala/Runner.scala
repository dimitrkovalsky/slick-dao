import dao.Employee

/**
 * User: Dimitr
 * Date: 08.03.14
 * Time: 12:12
 */
object Runner {
  def main(args: Array[String]) {

    val dao = DaoFactory.getEmployeeDao

    dao.insert(Employee("Simple", "simple@mail.com", Some("Some note")))

    for (obj <- dao.findPage(1, 10))
      println(obj)
  }
}
