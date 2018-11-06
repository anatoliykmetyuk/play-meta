package playmeta

import fastparse._, Parsed._
import better.files._, better.files.File._, java.io.{ File => JFile }

import ast._

object Main {
  def main(args: Array[String]): Unit = {
    val schema = file"dsl-src/schema.sql".contentAsString
    parse(schema, parser.createTables(_)) match {
      case Success(v, _) => println(v.mkString("\n\n")); generateSources(v)
      case f: Failure    => println(f)
    }
  }

  def generateSources(tables: List[CreateTable]): Unit = {
    val pkgname  = "gameforum.model"
    val outdir   = file"out/"
    val modelDir = file"$outdir/${pkgname.replace('.', '/')}"

    outdir.clear()
    modelDir.createDirectoryIfNotExists()
    tables.foreach { table =>
      val name   = gen.modelName(table.name)
      val source = gen.model(table, Some(pkgname))
      val file   = file"$modelDir/$name.scala"
      file.write(source)
    }
  }
}
