package com.github.twinra.lendmanager.repo.dao

import com.github.twinra.lendmanager.domain.Lending
import com.github.twinra.lendmanager.repo.CRUDRepository
import scalikejdbc._

class LendingRepository extends CRUDRepository[Lending, Long] {

  private val col = LendingDAO.column

  private val l = LendingDAO.syntax
  private val i = ItemDAO.syntax
  private val p = PersonDAO.syntax

  override def add(entity: Lending): Long = {
    val itemId = entity.item.id.getOrElse(throw new IllegalArgumentException("item id is required"))
    val personId = entity.person.id.getOrElse(throw new IllegalArgumentException("person id is required"))

    DB localTx { implicit session =>
      withSQL {
        insertInto(LendingDAO).namedValues(
          col.itemId -> itemId,
          col.personId -> personId,
          col.date -> entity.date.toString
        )
      }.updateAndReturnGeneratedKey().apply()
    }
  }

  override def update(id: Long, entity: Lending): Unit = throw new UnsupportedOperationException()

  override def delete(id: Long): Unit = DB localTx { implicit session =>
    withSQL { QueryDSL.delete.from(LendingDAO).where.eq(col.id, id) }.update().apply()
  }

  override def readAll(): Seq[Lending] = DB readOnly { implicit session =>
    withSQL {
      select.from(LendingDAO as l)
        .innerJoin(ItemDAO as i).on(l.itemId, i.id)
        .innerJoin(PersonDAO as p).on(l.personId, p.id)
    }.map(LendingDAO(l, i, p)).list().apply().map(_.lending)
  }

  override def read(id: Long): Option[Lending] = DB readOnly { implicit session =>
    withSQL {
      select.from(LendingDAO as l)
        .innerJoin(ItemDAO as i).on(l.itemId, i.id)
        .innerJoin(PersonDAO as p).on(l.personId, p.id)
        .where.eq(l.id, id)
    }.map(LendingDAO(l, i, p)).single().apply().map(_.lending)
  }


  override def drop(): Unit = DB localTx { implicit session => sql"drop table if exists ${LendingDAO.table}".execute().apply() }

  override def create(): Unit = DB localTx { implicit session =>
    sql"""create table if not exists ${LendingDAO.table} (
        id bigint auto_increment not null primary key,
        item_id bigint not null,
        person_id bigint not null,
        date DATE not null,
        foreign key (item_id) references ${ItemDAO.table}(id),
        foreign key (person_id) references ${PersonDAO.table}(id)
    )""".execute().apply()
  }
}
