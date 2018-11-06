package playmeta

import org.apache.commons.text.CaseUtils._  

import ast._

object gen {
  def model(ct: CreateTable, pkg: Option[String] = None): String = {
    val ident = ct.fields.map(_.name.length).max

    s"""
     |${pkg.map { p => s"package $p" }.getOrElse("")}
     |
     |case class ${modelName(ct.name)}(
     |  ${ct.fields.map { case Field(name, tpe, isNull, _) =>
          s"$name${" " * (ident - name.length)}: ${sqlTpeToScala(tpe)}"
        }.mkString("\n, ")}
     |)
    """.stripMargin.tail
  }

  def modelName(sqlTableName: String): String =
    toCamelCase(sqlTableName, true, '_')

  def sqlTpeToScala(tpe: String): String = tpe match {
    case "serial"    => "Option[Int] = None"
    case "varchar"   => "String"
    case "int4"      => "Int"
    case "text"      => "String"
    case "timestamp" => "Long"
  }
}
