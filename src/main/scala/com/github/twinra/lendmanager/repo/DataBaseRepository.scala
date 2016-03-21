package com.github.twinra.lendmanager.repo

import java.time.LocalDate

import com.github.twinra.lendmanager.domain.{Item, Person, Lending}

//TODO: implement see http://scalikejdbc.org/
class DataBaseRepository extends Repository {
  override def init(): Unit = ???

  override def addItem(item: Item): Item = ???

  override def items: Seq[Item] = ???

  override def lend(person: Person, item: Item, date: LocalDate): Lending = ???

  override def people: Seq[Person] = ???

  override def addPerson(person: Person): Person = ???

  override def lendings: Seq[Lending] = ???
}
