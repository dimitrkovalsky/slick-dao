package scala.slick.dao

import scala.slick.lifted.{AbstractTable, TableQuery}
import DBConnection.profile.simple._

/**
 * Created by Dmytro_Kovalskyi on 11.03.14.
 */
/**
 * Trait that defines all CRUD operation with their implementation
 * @tparam T Collection of entities
 * @tparam K Primary key
 */
trait CRUDable[T <: AbstractTable[_], K] extends Insertable[T] with Searchable[T, K]
with Updatable[T, K] with Removable[T, K] with Selectable[T, K]

sealed trait Requestable[T <: AbstractTable[_]] extends Profile {
  val entities: TableQuery[T]
  val session: Session
  implicit val innerSession = session
}

/**
 * Trait that defines how to find necessary entity
 * @tparam T Collection of entities
 * @tparam K Primary key
 */
trait Selectable[T <: AbstractTable[_], K] {
  def selectBy(entity: T#TableElementType): Query[T, T#TableElementType]

  def selectById(id: K): Query[T, T#TableElementType]
}

trait Insertable[T <: AbstractTable[_]] extends Requestable[T] {
  def insert(entity: T#TableElementType) {
    entities += entity
  }

  def insertAll(data: T#TableElementType*) {
    data.foreach(insert)
  }
}


trait Updatable[T <: AbstractTable[_], K] extends Requestable[T] {
  this: Selectable[T, K] =>

  def update(entity: T#TableElementType) {
    selectBy(entity).update(entity)
  }

  def update(id: K, entity: T#TableElementType) {
    selectById(id).update(entity)
  }
}

trait Removable[T <: AbstractTable[_], K] extends Requestable[T] {
  this: Selectable[T, K] =>

  def delete(entity: T#TableElementType) {
    selectBy(entity).mutate(_.delete())
  }

  def deleteById(id: K) {
    selectById(id).mutate(_.delete())
  }
}

/**
 * @tparam T collection of entities type
 */
trait Searchable[T <: AbstractTable[_], K] extends Requestable[T] {
  this: Selectable[T, K] =>

  def findAll(): List[T#TableElementType] = {
    entities.list
  }

  def findPage(pageNumber: Int, pageSize: Int): List[T#TableElementType] = {
    entities.drop(pageSize * (pageNumber - 1)).take(pageSize).list
  }

  def find(limit: Int): List[T#TableElementType] = {
    entities.take(limit).list
  }

  def findById(id: K): T#TableElementType = {
    selectById(id).first()
  }

  def count: Int = {
    Query(entities.length).first()
  }

}
