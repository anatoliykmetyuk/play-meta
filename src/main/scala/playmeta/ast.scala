package playmeta

object ast {
  case class CreateTable(name: String, fields: List[Field], constraints: List[Constraint]) {
    override def toString =
      s"Table $name:\nFields\n\t${fields.mkString("\n\t")}\nConstraints\n\t${constraints.mkString("\n\t")}"
  }

  sealed trait CreateTableArg

  case class Field(name    : String
                  , tpe    : String
                  , isNull : Boolean
                  , default: Option[String]) extends CreateTableArg

  sealed trait Constraint extends CreateTableArg { val name, field: String }
  case class   PrimaryKey(name: String, field: String) extends Constraint
  case class   Unique    (name: String, field: String) extends Constraint
  case class   ForeignKey(name: String, field: String
                        , refTable: String
                        , refField: String
                        , onDelete: Option[String])    extends Constraint
}
