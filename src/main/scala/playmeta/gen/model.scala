package playmeta
package gen

import org.apache.commons.text.CaseUtils._  

import ast._

object model extends Generator {
  val sqlToScalaTpeMap: Map[String, String] = Map(
    "serial"    -> "Int"
  , "varchar"   -> "String"
  , "int4"      -> "Int"
  , "text"      -> "String"
  , "timestamp" -> "Long"
  )

  def apply(ct: CreateTable, pkg: Option[String] = None): String = {
    val ident = ct.fields.map(_.name.length).max

    def isRef(tpe: String) = ct.constraints
      .collect { case x: PrimaryKey => x.field case x: ForeignKey => x.field }
      .contains(tpe)

    s"""
     |${pkg.map { p => s"package $p" }.getOrElse("")}
     |package ${subpackage}
     |
     |case class ${name(ct)}(
     |  ${ct.fields.map { case Field(name, tpe, isNull, _) =>
          s"$name${" " * (ident - name.length)}: ${sqlTpeToScala(tpe, isRef(name))}"
        }.mkString("\n, ")})
    """.stripMargin.tail
  }

  def name(ct: CreateTable): String =
    toCamelCase(ct.name, true, '_')

  val subpackage = "model"

  def sqlTpeToScala(tpe: String, isRef: Boolean): String = {
    val scalaTpe = sqlToScalaTpeMap(tpe)
    if (isRef) s"Option[$scalaTpe] = None" else scalaTpe
  }
}
