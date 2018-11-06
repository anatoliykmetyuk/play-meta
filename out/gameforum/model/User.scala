package gameforum.model

case class User(
  id        : Option[Int] = None
, username  : String
, password  : String
, first_name: String
, last_name : String
, address   : String)
    