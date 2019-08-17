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

  def postFromForm(post_id: Option[Int], params:(UUID, Option[Int]))
    = apply(None, params._1, post_id, params._2)

  def commentFromForm(comment_id: Option[Int], params:(UUID, Option[Int]))
    = apply(None, params._1, params._2 , comment_id)
}
