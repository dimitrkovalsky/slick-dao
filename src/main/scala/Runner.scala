import dao.{DaoFactory, Employee}
import scala.slick.dao.Generators

/**
 * User: Dimitr
 * Date: 08.03.14
 * Time: 12:12
 */
object Runner {
  def main(args: Array[String]) {

    val generator = Generators.getGenerator
    generator.generate("person", "src/main/scala", "dao", "PersonDao", updateFactory = true)
//    val dao = DaoFactory.getPersonDao
//
//    for (obj <- dao.findPage(1, 10))
//      println(obj)
  }
}
