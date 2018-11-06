// ForumMessage
def select = sql"select * from forum_topic"

def list: IO[List[ForumTopic]] = select
  .query[ForumTopic].to[List].transact(tr)

def create(topic: ForumTopic): EitherT[IO, String, Int] = EitherT { sql"""
  insert into forum_topic (title, author)
  values (${topic.title}, ${topic.author.get})
"""
  .update.withUniqueGeneratedKeys[Int]("id").transact(tr).attempt }
    .leftMap(_ => const.message.userAlreadyExists)

def find(id: Int): OptionT[IO, ForumTopic] = OptionT {
  (select ++ fr"where id = $id").query[ForumTopic].option.transact(tr) }


// ForumTopic
/* find topic */
def findByTopic(id: Int): IO[List[ForumMessage]] =
  (select ++ fr"where topic = $id")
    .query[ForumMessage].to[List].transact(tr)

// User
/*
find {
  (username, password)
  saltedHashId(hash: String) = encode(sha256(concat(
    id::text, ${config.sessionSalt})::bytea), 'hex') = $hash
}
*/
def findByUsernameAndPassword(username: String  // findBy<col>[And<col>...]
  , password: String): OptionT[IO, User] =
  OptionT { sql"""
    select * from users
    where username = $username and password = $password"""
    .query[User].option.transact(tr) }

def findBySaltedIdHash(hash: String): OptionT[IO, User] =  // findBy<custom>; where f(id) = $hash
  OptionT { sql"""
    select * from users
    where encode(sha256(concat(
      id::text, ${config.sessionSalt})::bytea), 'hex') = $hash
  """.query[User].option.transact(tr) }

def get(id: Int): IO[User] = sql"select * from users where id = $id"
  .query[User].unique.transact(tr)  // EitherT


// Session
/*
delete {
  session_id
  oldAge(maxAgeSeconds: Int) = extract(epoch from (now() - created_at)) > $maxAgeSeconds
}
*/
def deleteBySessionId(sessionId: String): IO[Int] =  // deleteBy<col>; where <col> = $inp
  sql"delete from sessions where session_id = $sessionId"
    .update.run.transact(tr)

def deleteByOldAge(maxAgeSeconds: Int): IO[Int] =  // deleteBy<custom>; where f(<col>) > $inp
  sql"""
    delete from sessions
    where extract(epoch from (now() - created_at)) > $maxAgeSeconds
  """
    .update.run.transact(tr)


// CreditCard
def createOrUpdate(cc: CreditCard): IO[Int] = sql"""
  insert into credit_cards (user_id, card_number, expiry)
  values (${cc.user_id}, ${cc.card_number}, ${cc.expiry})
  on conflict (user_id) do update
    set card_number = ${cc.card_number}
      , expiry      = ${cc.expiry     }
    returning id
  """
  .update.withUniqueGeneratedKeys[Int]("id").transact(tr)

