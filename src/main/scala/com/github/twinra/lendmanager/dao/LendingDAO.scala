package com.github.twinra.lendmanager.dao

import java.time.LocalDate

import com.github.twinra.lendmanager.domain.Lending
import scalikejdbc._

case class LendingDAO(id: Long, itemId: Long, personId: Long, date: String, item: ItemDAO, person: PersonDAO) {
  def lending: Lending = Lending(item.item, person.person, LocalDate.parse(date), Some(id))
}

object LendingDAO extends SQLSyntaxSupport[LendingDAO] {
  type LendingSP = SyntaxProvider[LendingDAO]
  type ItemSP = SyntaxProvider[ItemDAO]
  type PersonSP = SyntaxProvider[PersonDAO]

  type LendingRN = ResultName[LendingDAO]
  type ItemRN = ResultName[ItemDAO]
  type PersonRN = ResultName[PersonDAO]

  override val tableName = "Lendings"

  def apply(l: LendingSP, i: ItemSP, p: PersonSP)(rs: WrappedResultSet): LendingDAO = apply(l.resultName, i.resultName, p.resultName)(rs)

  def apply(l: LendingRN, i: ItemRN, p: PersonRN)(rs: WrappedResultSet): LendingDAO = {
    LendingDAO(
      rs.long(l.id),
      rs.long(l.itemId),
      rs.long(l.personId),
      rs.string(l.date),
      ItemDAO(i)(rs),
      PersonDAO(p)(rs)
    )
  }
}
