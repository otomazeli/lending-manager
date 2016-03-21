package com.github.twinra.lendmanager.domain

import scalikejdbc._

case class Item(name: String, id: Option[Long] = None)

object Item extends SQLSyntaxSupport[Item] {
  override val tableName = "Items"

  def apply(sp: SyntaxProvider[Item])(rs: WrappedResultSet): Item = apply(sp.resultName)(rs)
  def apply(rn: ResultName[Item])(rs: WrappedResultSet): Item = Item(rs.string(rn.name), rs.longOpt(rn.id))
}
