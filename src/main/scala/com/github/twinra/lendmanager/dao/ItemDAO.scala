package com.github.twinra.lendmanager.dao

import com.github.twinra.lendmanager.domain.Item
import scalikejdbc._

case class ItemDAO(name: String, id: Long) {
  def item: Item = Item(name, Some(id))
}

object ItemDAO extends SQLSyntaxSupport[ItemDAO] {
  override val tableName = "Items"

  def apply(sp: SyntaxProvider[ItemDAO])(rs: WrappedResultSet): ItemDAO = apply(sp.resultName)(rs)

  def apply(rn: ResultName[ItemDAO])(rs: WrappedResultSet): ItemDAO = ItemDAO(rs.string(rn.name), rs.long(rn.id))
}

