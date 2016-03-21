package com.github.twinra.lendmanager.repo

import java.time.LocalDate

import com.github.twinra.lendmanager.domain.{Item, Person, Lending}

import scala.collection.mutable.ArrayBuffer

class DummyRepository extends Repository {

  override def init(): Unit = {
    addPeople(Person("Vasya"), Person("Petya"), Person("Kolya"))
    addItems(Item("Autumn book"), Item("Stroller"), Item("Dishes"))
    lend(people.head, items.head, LocalDate.of(2016, 2, 15))
  }

  val items = ArrayBuffer.empty[Item]
  val people = ArrayBuffer.empty[Person]
  val lendings = ArrayBuffer.empty[Lending]

  override def lend(person: Person, item: Item, date: LocalDate = LocalDate.now): Lending = {
    require(person.id.nonEmpty)
    require(item.id.nonEmpty)
    require(isItemFree(item), "Item is already borrowed")

    val result = Lending(item, person, date, Some(nextBorrowingId))
    lendings.append(result)
    result
  }

  override def addItem(item: Item): Item = {
    require(item.id.isEmpty)
    val result = item.copy(id = Some(nextItemId))
    items.append(result)
    result
  }

  override def addPerson(person: Person): Person =  {
    require(person.id.isEmpty)
    val result = person.copy(id = Some(nextPersonId))
    people.append(result)
    result
  }

  // TODO: generalize it with Identified trait
  private def nextBorrowingId = lendings.flatMap(_.id) match {
    case seq if seq.isEmpty => 0L
    case seq => seq.max + 1
  }
  private def nextItemId = items.flatMap(_.id) match {
    case seq if seq.isEmpty => 0L
    case seq => seq.max + 1
  }
  private def nextPersonId = people.flatMap(_.id) match {
    case seq if seq.isEmpty => 0L
    case seq => seq.max + 1
  }
}
