package playmeta

case class CreateTable(name: String, fields: List[Field])
case class Field(name: String, tpe: String, isNull: Boolean)
