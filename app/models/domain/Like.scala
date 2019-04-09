package models.domain

case class Like (
    id: Int,
    userID: java.util.UUID,
    postID: Int,
    commentID: Int,
    commentReplyID: Int,
    createdAt: java.time.Instant)
