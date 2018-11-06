package playmeta

import fastparse._, Parsed._

object Main {
  val stmt = """
CREATE TABLE users (
  id         serial  NOT NULL
, username   varchar NOT NULL
, password   varchar NOT NULL
, first_name varchar NOT NULL
, last_name  varchar NOT NULL
, address    varchar NOT NULL

, CONSTRAINT users_pk PRIMARY KEY (id)
, CONSTRAINT users_un UNIQUE (username)
);

CREATE TABLE forum_topic (
  id     serial  NOT NULL
, title  varchar NOT NULL
, author int4    NOT NULL

, CONSTRAINT forum_topic_pk       PRIMARY KEY (id)
, CONSTRAINT forum_topic_users_fk FOREIGN KEY (author) REFERENCES users(id)
);

CREATE TABLE forum_message (
  id         serial    NOT NULL
, content    text      NOT NULL
, author     int4      NOT NULL
, topic      int4      NOT NULL
, created_at timestamp NOT NULL DEFAULT now()

, CONSTRAINT forum_message_pk             PRIMARY KEY (id)
, CONSTRAINT forum_message_forum_topic_fk FOREIGN KEY (topic ) REFERENCES forum_topic(id) ON DELETE CASCADE
, CONSTRAINT forum_message_users_fk       FOREIGN KEY (author) REFERENCES users(id)
);

"""

  def main(args: Array[String]): Unit = {
    val result = parse(stmt, SchemaParser.createTables(_))
    result match {
      case Success(v, _) => println(v.mkString("\n\n"))
      case f: Failure    => println(f)
    }
  }
}
