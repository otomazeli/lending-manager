package com.github.twinra.lendmanager.repo

import java.time.LocalDate

import com.github.twinra.lendmanager.domain.{Lending, Item, Person}

//TODO: split it into poor and reach parts(cake pattern)
trait Repository {
  def init(): Unit

  def people: Seq[Person]
  def items: Seq[Item]
  def lendings: Seq[Lending]

  def personById(id: Long): Option[Person] = people.find(_.id.contains(id))
  def itemById(id: Long): Option[Item] = items.find(_.id.contains(id))
  def borrowingById(id: Long): Option[Lending] = lendings.find(_.id.contains(id))

  def itemsLendedTo(person: Person): Seq[Item] = {
    require(person.id.nonEmpty)
    lendings.filter(_.person.id == person.id).map(_.item)
  }
  def isItemLended(item: Item): Boolean = {
    require(item.id.nonEmpty)
    lendings.map(_.item.id).contains(item.id)
  }
  def isItemFree(item: Item): Boolean = !isItemLended(item)

  def addItem(item: Item): Item
  def addPerson(person: Person): Person
  def lend(person: Person, item: Item, date: LocalDate): Lending

  def addItems(itemsToAdd: Item*) = itemsToAdd.map(addItem)
  def addPeople(peopleToAdd: Person*) = peopleToAdd.map(addPerson)
  def lend(person: Person, items: Seq[Item], date: LocalDate): Seq[Lending] = items.map(lend(person, _, date))

  //TODO: add methods to update & delete entities
}
