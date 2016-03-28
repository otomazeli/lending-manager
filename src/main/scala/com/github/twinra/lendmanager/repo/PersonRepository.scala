package com.github.twinra.lendmanager.repo

import com.github.twinra.lendmanager.domain.Person
import com.github.twinra.lendmanager.repo.dao.PersonDAO
import scalikejdbc._

class PersonRepository extends CRUDRepository[Person, Long] {

  private val col = PersonDAO.column
  private val itm = PersonDAO.syntax

  override def add(entity: Person): Long = DB localTx { implicit session =>
    withSQL {
      insertInto(PersonDAO).namedValues(col.name -> entity.name)
    }.updateAndReturnGeneratedKey().apply()
  }

  override def update(id: Long, entity: Person): Unit = DB localTx { implicit session =>
    withSQL {
      QueryDSL.update(PersonDAO).set(col.name -> entity.name).where.eq(col.id, id)
    }.update().apply()
  }

  override def delete(id: Long): Unit = DB localTx { implicit session =>
    withSQL {
      QueryDSL.delete.from(PersonDAO).where.eq(col.id, id)
    }.update().apply()
  }

  override def readAll(): Seq[Person] = DB readOnly { implicit session =>
    withSQL {
      select(itm.result.*).from(PersonDAO as itm)
    }.map(PersonDAO(itm)).list().apply().map(_.person)
  }

  override def read(id: Long): Option[Person] = DB readOnly { implicit session =>
    withSQL {
      select.from(PersonDAO as itm).where.eq(itm.id, id)
    }.map(PersonDAO(itm)).single().apply().map(_.person)
  }

  override def drop(): Unit = DB localTx { implicit session => sql"drop table if exists ${PersonDAO.table}".execute().apply() }

  override def create(): Unit = DB localTx { implicit session =>
    sql"""create table if not exists ${PersonDAO.table} (
         id bigint auto_increment not null primary key,
         name varchar(64))"""
      .execute().apply()
  }
}
