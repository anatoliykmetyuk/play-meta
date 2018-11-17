package playmeta

import cats._, cats.implicits._, cats.data._, cats.effect._
import better.files._, better.files.File._, java.io.{ File => JFile }
import thera._, thera.runtime._, Context.names
import io.circe._

object Main extends App {
  val localFunctions = names(
    "opt" -> function[Text, Runtime] { (varName, elseCond) =>
      State.get[Context] >>= { ctx =>
        ifFunc(varName.value, ctx(varName.value), elseCond) } }
  )

  implicit val ctx = predef.ctx |+| localFunctions


  val yamlRaw: String = file"data/model.yaml".contentAsString
  val data: Map[String, Json] =
    yaml.parser.parse(yamlRaw).right.get.asObject.get.toMap

  val template = compile(file"src-tml/schema.sql".contentAsString).asFunc
  val (name, spec) = data.toList.head

  println(template(name, spec).asString)
}
