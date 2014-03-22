import dao.{DaoFactory, Employee}
import scala.slick.dao.Generators

/**
 * User: Dimitr
 * Date: 08.03.14
 * Time: 12:12
 */
object Runner {
  def main(args: Array[String]) {

    val generator = Generators.createGenerator("person", "src/main/scala", "dao", "PersonDao")
    generator.generateDao(updateFactory = true)

   // generator.generateUnitTest()
  }
}
