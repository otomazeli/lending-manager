package com.github.twinra.utils.json

import java.time.LocalDate

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

case object JLocalDateSerializer extends CustomSerializer[LocalDate](format => (
  {
    case JString(s) => LocalDate.parse(s)
    case JNull => null
  },
  {
    case ld: LocalDate => JString(ld.toString)
  }
))