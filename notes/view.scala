// newMessage
create.html
@(form: Form[ForumMessage])
form(route, form)

// topic
@(topic: ForumTopic, messages: List[ForumMessage])(
  implicit request: RequestHeader)

@gameforum.view.html.main(s"Forum | ${topic.title}") {
  <h1>@topic.title</h1>
  <a href="@rts.Forum.topics">Forum</a>
  <a href="@rts.Forum.newMessage(topic.id.get)">Reply</a>

  @messages.map { message =>
    <div>
      message.fileds.map { ... }
    </div>
    <hr/>
  }

  <a href="@rts.Forum.newMessage(topic.id.get)">Reply</a>
}
