package playmeta

import fastparse._

object Main {
  def main(args: Array[String]): Unit = {
    val result = parse("CREATE TABLE users", SchemaParser.createTable(_))
    println(result)
  }
}
