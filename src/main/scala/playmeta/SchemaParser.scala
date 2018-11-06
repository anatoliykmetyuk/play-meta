package playmeta

import fastparse._, MultiLineWhitespace._

object SchemaParser {
  object keyword {
    def create[_: P] = IgnoreCase("create")
    def table [_: P] = IgnoreCase("table" )
    def tpe[_: P] = "serial" | "varchar" | "int4" | "text" | "timestamp"
    def default[_: P] = IgnoreCase("default")
    def not[_: P] = IgnoreCase("not")
    def nul[_: P] = IgnoreCase("null")
  }
  val k = keyword

  object token {
    def name[_: P] = CharIn("a-zA-Z0-9_").repX(1)
    def sqlExpr[_: P] = CharIn("a-zA-Z\\s()")
  }
  val t = token

  def createTable[_: P]: P[CreateTable] =
    P( k.create ~ k.table ~ t.name.! ~ "(" ~ createTableArgs ~ ")" ~ ";").map { case (name, fields) =>
      CreateTable(name, fields.toList) }

  def createTableArgs[_: P]: P[Seq[Field]] =
    field.rep(sep = ",")

  def field[_: P]: P[Field] =
    P( t.name.! ~ k.tpe.! ~ isNull ~ default.? ).map { case (name, tpe, isNull) =>
      Field(name, tpe, isNull) }

  def isNull[_: P]: P[Boolean] = (k.not.?.! ~ k.nul).map(_.isEmpty)

  def default[_: P] = k.default ~ t.sqlExpr
}
