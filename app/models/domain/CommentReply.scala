package models.domain

case class CommentReply(
    commentID: Int,
    content: String,
    likeFlag: Boolean,
    createdAt: java.time.Instant)
