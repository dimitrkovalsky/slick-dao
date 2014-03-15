package scala.slick.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.ConfigFactory
import scala.slick.driver.{JdbcProfile, PostgresDriver}

object DBConnection extends Profile {
  val profile:JdbcProfile = PostgresDriver

  import profile.simple._
  val env = scala.util.Properties.envOrElse("runMode", "prod")
  val config = ConfigFactory.load(env)
  val url = config.getString("db.url")
  val username = config.getString("db.username")
  val password = config.getString("db.password")
  val driver = config.getString("db.driver")


 // val database = Database.forURL(url, username, password, null, driver)

  val databasePool = {

    val ds = new ComboPooledDataSource
    ds.setDriverClass(driver)
    ds.setJdbcUrl(url)
    ds.setUser(username)
    ds.setPassword(password)
    ds.setMinPoolSize(5)
    ds.setAcquireIncrement(5)
    ds.setMaxPoolSize(100)
    Database.forDataSource(ds)
  }
}