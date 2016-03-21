package com.github.twinra.lendmanager.domain

import java.time.LocalDate

case class Lending(item: Item, person: Person, date: LocalDate, id: Option[Long] = None)

