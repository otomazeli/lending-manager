package com.github.twinra.utils.json

import java.time.LocalDate

import com.github.twinra.lendmanager.domain.Lending
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import org.scalatest.{FlatSpec, Matchers}


class JLocalDateSerializerTest extends FlatSpec with Matchers {

  private case class Borrow(itemId: Long, personId: Long, date: LocalDate)
  implicit lazy val formats = DefaultFormats + JLocalDateSerializer

  it should "parse LocalDate properly" in {
    val json = "{\"person\":{\"name\":\"Roman\"},\"date\":\"2016-03-19\",\"item\":{\"name\":\"Book\"}}"
    val parsed = parse(json).extract[Lending]

    parsed.date should be (LocalDate.of(2016,3,19))
  }

  it should "parse LocalDate properly even for internal case classes" in {
    val json = "{\"itemId\":0,\"personId\":0,\"date\":\"2016-03-19\"}"
    val parsed = parse(json).extract[Borrow]

    parsed.date should be (LocalDate.of(2016,3,19))
  }
}
