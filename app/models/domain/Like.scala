package models.domain
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json.Writes
import java.util.UUID

case class Like(
  id: Option[Int],
  userID: UUID,
  postID: Option[Int],
  commentID: Option[Int])

object Like {
  val tupled = (Like.apply _).tupled //tuple convert to case class→db insert with tuple

  implicit val writes: Writes[Like] = Json.writes[Like]

  def postFromForm(user_id: UUID, post_id: Option[Int])
    = apply(None, user_id, post_id, None)

  def commentFromForm(user_id: UUID, comment_id: Option[Int])
    = apply(None, user_id, None, comment_id)
}
