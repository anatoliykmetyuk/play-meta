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
    import gen._

    val pkgname  = "gameforum"
    val outdir   = file"out/"

    val generators: List[Generator] = List(db, model)

    outdir.clear()

    for {
      table     <- tables
      generator <- generators
    } {
      val name   = generator.name(table)
      val subpkg = generator.subpackage
      val source = generator(table, Some(pkgname))
      val genDir = file"$outdir/$pkgname/$subpkg/"
      val file   = file"$genDir/$name.scala"

      genDir.createDirectoryIfNotExists()
      file.write(source)    
    }
  }
}
