package playmeta

import fastparse._

object Main {
  val stmt = """CREATE TABLE users (
  id         serial  NOT NULL
, username   varchar NOT NULL
, password   varchar NOT NULL
, first_name varchar     NULL
, last_name  varchar NOT NULL
, address    varchar NOT NULL
);
"""

  def main(args: Array[String]): Unit = {
    val result = parse(stmt, SchemaParser.createTable(_))
    println(result)
  }
}
