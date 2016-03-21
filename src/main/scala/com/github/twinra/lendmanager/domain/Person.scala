package com.github.twinra.lendmanager.domain

import scalikejdbc._

case class Person(name: String, id: Option[Long] = None)

object Person extends SQLSyntaxSupport[Person] {
  override val tableName = "People"

  def apply(sp: SyntaxProvider[Person])(rs: WrappedResultSet): Person = apply(sp.resultName)(rs)
  def apply(rn: ResultName[Person])(rs: WrappedResultSet): Person = Person(rs.string(rn.name), rs.longOpt(rn.id))
}

