package gameforum.model

case class ForumMessage(
  id        : Option[Int] = None
, content   : String
, author    : Option[Int] = None
, topic     : Option[Int] = None
, created_at: Long)
    