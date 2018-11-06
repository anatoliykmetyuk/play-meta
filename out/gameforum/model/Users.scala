package gameforum.model

case class Users(
  id        : Option[Int] = None
, username  : String
, password  : String
, first_name: String
, last_name : String
, address   : String
)
    