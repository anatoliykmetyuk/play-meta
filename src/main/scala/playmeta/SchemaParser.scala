package playmeta

import fastparse._, ScalaWhitespace._

object SchemaParser {
  object keyword {
    def create[_: P] = IgnoreCase("create")
    def table [_: P] = IgnoreCase("table" )
  }
  val k = keyword

  object token {
    def name[_: P] = CharIn("a-zA-Z0-9_").repX(1)
  }
  val t = token

  def createTable[_: P]: P[String] =
    (k.create ~ k.table ~ t.name.!).map { str => s"Creating table $str" }
}
