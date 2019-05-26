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
  val tupled = (apply _).tupled

  implicit val writes: Writes[Like] = Json.writes[Like]
}
