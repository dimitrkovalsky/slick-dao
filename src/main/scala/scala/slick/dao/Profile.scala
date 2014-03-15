package scala.slick.dao

import scala.slick.driver.JdbcProfile

trait Profile {
  val profile: JdbcProfile
}
