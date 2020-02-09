package models.domain

case class CommentReply(
    commentID: Int,
    content: String,
    createdAt: java.time.Instant)
