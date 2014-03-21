package dao

import org.junit.Assert._
import org.junit.Test

class PersonDaoTest {
  @Test def verifyFindAll() {
    val dao = DaoFactory.getPersonDao
    val count = dao.count
    assertEquals(count, dao.findAll().size)
  }
}
