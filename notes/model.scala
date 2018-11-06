sealed trait Ref[+A] {
  def resolveOpt: OptionT[IO, A] = this match {
    case Empty => None
    case Primitive(id) => db.$table.find(id)
    case Resolved (v ) => v
  }

  def resolve: IO[A]
  def resolve: Either[IO, String, A]
  def resolveUnsafe: A
}

object Empty extends Ref[Nothing]
case class Primitive(value: Int) extends Ref[Nothing]
case class Resolved[T](value: T) extends Ref[T]

case class ForumMessage(
  id        : Ref[ForumMessage] = Empty
, content   : String
, author    : Ref[User      ] = Empty
, topic     : Ref[ForumTopic] = Empty
, created_at: Long)

case class ForumTopic(
  id    : Ref[ForumTopic] = Empty
, title : String
, author: Ref[User] = Empty)

case class User(
  id        : Ref[User] = Empty
, username  : String
, password  : String
, first_name: String
, last_name : String
, address   : String)
