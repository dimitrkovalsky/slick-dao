package scala.slick.dao

import scala.slick.driver.JdbcProfile
 import DBConnection.profile.simple._
/**
 * User: Dimitr
 * Date: 08.03.14
 * Time: 14:40
 */
abstract class AbstractDAO(implicit innerSession: Session) {

  val profile: JdbcProfile = DBConnection.profile
  val session: Session = innerSession


}

