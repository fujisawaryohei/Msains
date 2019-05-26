package models.domain
import java.util.UUID

case class Like(
  userID: UUID,
  postID: Int,
  commentID: Int
)
