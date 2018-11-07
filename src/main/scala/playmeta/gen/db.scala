package playmeta
package gen

import org.apache.commons.text.CaseUtils._  

import ast._
import gen.util._

object db extends Generator {
  // implicit class IndentString(str: String) {
  //   def indent(width: Int): String =
  //     str.split("\n").map(line => " " * width + line).mkString("\n")
  // }
  def apply(ct: CreateTable, pkg: Option[String] = None): String = {
    val pkgString = s"""
      |${pkg.map { p => s"package $p" }.getOrElse("")}
      |package ${subpackage}""".stripMargin.tail

    val imports = s"""
      |import doobie._, doobie.implicits._
      |import cats._, cats.implicits._, cats.data._, cats.effect._
      |
      |import infrastructure.tr
      |import model._""".stripMargin.tail

    def sql(str: String) =
      s"""sql${"\""*3}${str.stripMargin.tail.indent(6)}${"\""*3}""".stripMargin

    val selectSql = {
      val stmt = sql(s"""
       |select
       |${ct.fields.map(_.name).mkString("\n, ").indent(2)}
       |from ${ct.name}""".stripMargin)
      s"""val selectSql =
         |  $stmt""".stripMargin
    }


    val tpe = model.name(ct)

    val create = {
      val fieldNames = ct.fields.map(_.name).filter(_ != "id")

      val stmt = sql(s"""
      |insert into forum_topic (${fieldNames.mkString(", ")})
      |values (${fieldNames.map { n => s"$${model.$n}" }.mkString(", ")})""")

      s"""
      |def create(model: ${tpe}): IO[Int] =
      |  ${stmt}
      |    .update.withUniqueGeneratedKeys[Int]("id").transact(tr)""".stripMargin.tail
    }

    val list = s"""
     |val list: IO[List[${tpe}]] =
     |  selectSql.query[${tpe}].to[List].transact(tr)""".stripMargin.tail

    s"""
     |$pkgString
     |
     |$imports
     |
     |object ${name(ct)} {
     |  ${selectSql.indent(2)}
     |
     |  ${create.indent(2)}
     |
     |  ${list.indent(2)}
     |
     |{find.indent(2)}
     |
     |{get.indent(2)}
     |
     |{delete.indent(2)}
     |}
    """.stripMargin.tail
  }

  def name(ct: CreateTable): String =
    toCamelCase(ct.name, false, '_')

  val subpackage = "db"
}
