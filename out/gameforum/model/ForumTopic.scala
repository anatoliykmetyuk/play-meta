package gameforum.model

case class ForumTopic(
  id    : Option[Int] = None
, title : String
, author: Option[Int] = None)
    