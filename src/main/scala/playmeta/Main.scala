package playmeta

import cats._, cats.implicits._, cats.data._, cats.effect._
import better.files._, better.files.File._, java.io.{ File => JFile }
import thera._, thera.runtime._, thera.predef._, Context.names
import io.circe._

object Main extends App {
  val localFunctions = names(
    "opt" -> function[Text, Runtime] { (varName, elseCond) =>
      State.get[Context].map { ctx =>
        ctx.applyOpt(varName.value.split("\\.").toList).getOrElse(elseCond) } }
  )

  implicit val ctx = predefContext |+| localFunctions


  val yamlRaw: String = file"data/model.yaml".contentAsString
  val data: Map[String, Json] =
    yaml.parser.parse(yamlRaw).right.get.asObject.get.toMap

  val template = compile(file"src-tml/schema.sql".contentAsString).asFunc
  val (name, spec) = data.toList.head

  println(template(name, spec).asString)
}
