package com.github.twinra.lendmanager.repo

trait CRUDRepository[T, ID] {
  def add(entity: T): ID

  def read(id: ID): Option[T]

  def readAll(): Seq[T]

  def update(id: ID, entity: T): Unit

  def delete(id: ID)

  def init(): Unit

  def destroy(): Unit
}

