---
template: default
subpackage: db
---

import doobie._, doobie.implicits._
import cats._, cats.implicits._, cats.data._, cats.effect._

import infrastructure.tr
import model._

object #name {
  val selectSql =
    sql"""select
          #commaList #fields
          from #table"""

  def create(model: #tpe): IO[Int] =
    sql"""insert into #table (
          #commaList #fields)
          values (
          #commaList #fields.map { f => $#f })
      """.update.withUniqueGeneratedKeys[Int]("id").transact(tr)
    }

  def list: IO[List[#tpe]] =
    selectSql.query[#{tpe}].to[List].transact(tr)

  def find(id: Int): OptionT[IO, #tpe] =
    OptionT { (selectSql ++ fr"where id = $id")
      .query[#tpe].option.transact(tr) }

  def get(id: Int): IO[#tpe] =
    selectSql.query[#tpe].unique.transact(tr)

  def delete(id: Int): IO[Int] =
    sql"delete from #table where id = $id"
      .update.run.transact(tr)
}
