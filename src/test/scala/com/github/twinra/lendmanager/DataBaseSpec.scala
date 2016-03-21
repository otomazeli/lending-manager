package com.github.twinra.lendmanager

import com.github.twinra.lendmanager.domain.Item
import org.scalatest.{FlatSpec, Matchers}
import scalikejdbc._

class DataBaseSpec extends FlatSpec with Matchers {

  it should "work" in {
    //connect
    ConnectionPool.singleton("jdbc:mysql://localhost:3306/learn", "roman", "1234")

    // create & populate
    DB autoCommit { implicit session =>
      sql"create table if not exists Items (id serial not null primary key, name varchar(64))".execute().apply()

      Seq("Book", "Stroller", "Notebook").foreach { name =>
        sql"insert into Items (name) values ($name)".update().apply()
      }
    }

    // check
    DB readOnly { implicit session =>
      val count = sql"select count(*) from Items".map(_.long(1)).single().apply()
      count should be (Some(3))

      val extractName = (rs: WrappedResultSet) => rs.string("name")
      val names = sql"select name from Items".map(extractName).list().apply()
      names should be (List("Book", "Stroller", "Notebook"))

      val i = Item.syntax
      val bookItem = withSQL { select.from(Item as i).where.eq(i.name, "Book") }.map(Item(i)).single().apply()
      bookItem should not be None
    }
  }
}
