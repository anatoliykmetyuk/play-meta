package gameforum.model

case class ForumMessage(
  id        : Option[Int] = None
, content   : String
, author    : Int
, topic     : Int
, created_at: Long
)
    