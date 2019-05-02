package models.domain

case class Comment(
    id: Int,
    userID: java.util.UUID,
    postID: Int,
    content: String,
    likeFlag: Boolean,
    createdAt: java.time.Instant)
