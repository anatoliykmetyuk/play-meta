package playmeta

import fastparse._, MultiLineWhitespace._

import ast._

object parser {
  object keyword {
    def create[_: P] = IgnoreCase("create")
    def table [_: P] = IgnoreCase("table" )

    def tpe    [_: P] = "serial" | "varchar" | "int4" | "text" | "timestamp"
    def default[_: P] = IgnoreCase("default")
    def not    [_: P] = IgnoreCase("not")
    def nul    [_: P] = IgnoreCase("null")

    def constraint[_: P] = IgnoreCase("constraint")
    def primary   [_: P] = IgnoreCase("primary"   )
    def key       [_: P] = IgnoreCase("key"       )
    def unique    [_: P] = IgnoreCase("unique"    )
    def foreign   [_: P] = IgnoreCase("foreign"   )
    def references[_: P] = IgnoreCase("references")
    def on        [_: P] = IgnoreCase("on"        )
    def delete    [_: P] = IgnoreCase("delete"    )
    def cascade   [_: P] = IgnoreCase("cascade"   )
    def onDeleteOp[_: P] = cascade
  }
  val k = keyword

  object token {
    def name[_: P] = CharIn("a-zA-Z0-9_").repX(1)
    def sqlExpr[_: P] = CharIn("a-zA-Z()").repX(1)
  }
  val t = token

  def createTables[_: P]: P[List[CreateTable]] =
    Pass ~ createTable.rep.map(_.toList)

  def createTable[_: P]: P[CreateTable] =
    P( k.create ~ k.table ~ (t.name.! | "\"" ~ t.name.! ~ "\"") ~ "(" ~ createTableArgs ~ ")" ~ ";").map { case
      (name, args) => CreateTable(
          name
        , args.collect { case x: Field      => x }.toList
        , args.collect { case x: Constraint => x }.toList) }

  def createTableArgs[_: P]: P[Seq[CreateTableArg]] =
    (field | constraint).rep(sep = ",")

  def field[_: P]: P[Field] =
    P( t.name.! ~ k.tpe.! ~ isNull ~ default.?).map { case (name, tpe, isNull, default) =>
      Field(name, tpe, isNull, default) }

  def constraint[_: P]: P[Constraint] =
    P ( (k.constraint ~/ t.name.!).flatMap { name =>
      primaryKey(name) | foreignKey(name) | uniqueKey(name) } )


  def isNull[_: P]: P[Boolean] = (k.not.?.! ~ k.nul).map(_.isEmpty)

  def default[_: P]: P[String] = k.default ~ t.sqlExpr.!


  def primaryKey[_: P](name: String): P[PrimaryKey] =
    (k.primary ~ k.key ~ "(" ~ t.name.! ~ ")").map(PrimaryKey(name, _))

  def uniqueKey[_: P](name: String): P[Unique] =
    (k.unique ~ "(" ~ t.name.! ~ ")").map(Unique(name, _))

  def foreignKey[_: P](name: String): P[ForeignKey] =
    ( k.foreign ~ k.key ~ "(" ~ t.name.! ~ ")" ~
      k.references ~ t.name.! ~ "(" ~ t.name.! ~ ")" ~
      (k.on ~ k.delete ~ k.onDeleteOp.!).?
    ).map { case (field, refTable, refField, onDelete) =>
      ForeignKey(name, field, refTable, refField, onDelete) }
}
