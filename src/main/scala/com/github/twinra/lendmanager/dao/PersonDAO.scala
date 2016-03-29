package com.github.twinra.lendmanager.dao

import com.github.twinra.lendmanager.domain.Person
import scalikejdbc._


case class PersonDAO(name: String, id: Long) {
  def person: Person = Person(name, Some(id))
}

object PersonDAO extends SQLSyntaxSupport[PersonDAO] {
  override val tableName = "People"

  def apply(sp: SyntaxProvider[PersonDAO])(rs: WrappedResultSet): PersonDAO = apply(sp.resultName)(rs)

  def apply(rn: ResultName[PersonDAO])(rs: WrappedResultSet): PersonDAO = PersonDAO(rs.string(rn.name), rs.long(rn.id))
}
