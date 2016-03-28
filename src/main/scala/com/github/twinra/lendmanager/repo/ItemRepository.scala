package com.github.twinra.lendmanager.repo

import com.github.twinra.lendmanager.domain.Item
import com.github.twinra.lendmanager.repo.dao.ItemDAO
import scalikejdbc._

class ItemRepository extends CRUDRepository[Item, Long] {

  private val col = ItemDAO.column
  private val i = ItemDAO.syntax

  override def add(entity: Item): Long = DB localTx { implicit session =>
    withSQL {
      insertInto(ItemDAO).namedValues(col.name -> entity.name)
    }.updateAndReturnGeneratedKey().apply()
  }

  override def update(id: Long, entity: Item): Unit = DB localTx { implicit session =>
    withSQL {
      QueryDSL.update(ItemDAO).set(col.name -> entity.name).where.eq(col.id, id)
    }.update().apply()
  }

  override def delete(id: Long): Unit = DB localTx { implicit session =>
    withSQL { QueryDSL.delete.from(ItemDAO).where.eq(col.id, id) }.update().apply()
  }

  override def readAll(): Seq[Item] = DB readOnly { implicit session =>
    withSQL {
      select(i.result.*).from(ItemDAO as i)
    }.map(ItemDAO(i)).list().apply().map(_.item)
  }

  override def read(id: Long): Option[Item] = DB readOnly { implicit session =>
    withSQL {
      select.from(ItemDAO as i).where.eq(i.id, id)
    }.map(ItemDAO(i)).single().apply().map(_.item)
  }

  override def drop(): Unit = DB localTx { implicit session => sql"drop table if exists ${ItemDAO.table}".execute().apply() }

  override def create(): Unit = DB localTx { implicit session =>
    sql"""create table if not exists ${ItemDAO.table} (
         id bigint auto_increment not null primary key,
         name varchar(64))"""
      .execute().apply()
  }
}
