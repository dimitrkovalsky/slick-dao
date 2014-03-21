package scala.slick.dao

import scala.slick.jdbc.meta.{MQName, MTable}
import java.io.FileWriter
import scala.slick.model.Model
import scala.slick.model.codegen.SourceCodeGenerator
import scala.slick.ast.ColumnOption
import scala.io.Source

/**
 * Created by Dmytro_Kovalskyi on 20.03.2014.
 */
object Generators {
  private val scheme = "public"

  private implicit def session = DBConnection.databasePool.createSession()

  def createGenerator(tableName: String, folder: String, packag: String, daoClass: String) =
    new Generator(tableName, folder, packag, daoClass)

  class Generator(tableName: String, folder: String, packag: String, daoClass: String) {

    import scala.slick.jdbc.meta.createModel

    def generateDao(updateFactory: Boolean = false, factoryPath: Option[String] = None) {
      val mTable = MTable(MQName(None, Some(scheme), tableName), "TABLE", null, None, None, None)
      val model = createModel(Seq(mTable), DBConnection.profile)
      val codeGen = new DaoSourceGenerator(model)
      writeToFile(codeGen, folder, packag, daoClass)

      def updateDaoFactory() {
        val fileName = factoryPath.getOrElse(s"$folder/$packag/DaoFactory.scala")
        val lines = Source.fromFile(fileName).getLines().toList
        val string = s"  def get$daoClass: I$daoClass = new $daoClass()\n}"
        val result = lines.slice(0, lines.size - 1).mkString("\n") :: string :: Nil
        writeResult(fileName, result.mkString("\n"))
      }
      if (updateFactory)
        updateDaoFactory()
    }

    private def writeResult(path: String, data: String) {
      val writer = new FileWriter(path)
      writer.write(data)
      writer.close()
    }

    def generateUnitTest(factoryPath: Option[String] = None) {
      val factory = factoryPath.getOrElse(s"$folder/$packag/DaoFactory.scala")
      val test = s"""package $packag

import org.junit.Assert._
import org.junit.Test

class ${daoClass}Test {
  @Test def verifyFindAll() {
    val dao = DaoFactory.get${daoClass}
    val count = dao.count
    assertEquals(count, dao.findAll().size)
  }
}
"""
      writeResult(s"${folder.replace("main", "test")}/$packag/${daoClass}Test.scala", test)
    }

    def writeToFile(generator: DaoSourceGenerator, folder: String, packag: String, daoClass: String) {
      val builder = new StringBuilder
      builder.append(s"package $packag\n")
      builder.append("import scala.slick.dao.DBConnection.profile.simple._\n")
      builder.append("import scala.slick.dao._\n")
      builder.append(generator.code)
      builder ++= s"\n\ntrait I$daoClass extends AbstractDAO with CRUDable[${generator.table}, ${generator.pkType}]\n\n"
      val daoCode = s"""class $daoClass(implicit innerSession: Session) extends I$daoClass {

  val entities: TableQuery[${generator.table}] = TableQuery[${generator.table}]\n
  def selectBy(entity: ${generator.entity}) = {
    for (e <- entities if e.${generator.pkName} === entity.${generator.pkName}) yield e
  }

  def selectById(id: ${generator.pkType}) = {
    for (e <- entities if e.${generator.pkName} === id) yield e
  }
}"""
      builder ++= daoCode
      writeResult(s"$folder/$packag/${generator.entity}.scala", builder.toString())
    }
  }

}

class DaoSourceGenerator(model: Model) extends SourceCodeGenerator(model) {
  var table: String = _
  var entity: String = _
  var pkName: String = _
  var pkType: String = _

  // override mapped table and class name
  override def entityName =
    dbTableName => {
      entity = dbTableName.toLowerCase.toCamelCase
      entity
    }

  override def tableName =
    dbTableName => {
      table = dbTableName.toLowerCase.toCamelCase + "s"
      table
    }

  // add some custom import
  override def code = super.code.substring(super.code.indexOf("\n"))

  // override table generator
  override def Table = new Table(_) {
    // disable entity class generation and mapping
    override def EntityType = new EntityType {
      override def classEnabled = true
    }

    override def PlainSqlMapper = new PlainSqlMapperDef {
      override def enabled: Boolean = false
    }

    override def TableValue = new TableValueDef {
      override def enabled: Boolean = false
    }

    override def Index = new Index(_) {
      override def enabled: Boolean = false
    }

    override def TableClass = new TableClassDef {
      override def optionEnabled: Boolean = false
    }

    // override contained column generator
    override def Column = new Column(_) {
      // use the data model member of this column to change the Scala type, e.g. to a custom enum or anything else
      override def rawType = {
        if (model.options.contains(ColumnOption.PrimaryKey)) {
          pkName = super.rawName
          pkType = super.rawType.toString
        }
        super.rawType
      }
    }
  }
}
