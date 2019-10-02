package models.domain
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json.Writes
import java.util.UUID
import java.time.Instant

case class Comment(
    id: Option[Int],
    userID: java.util.UUID,
    postID: Int,
    content: String,
    createdAt: java.time.Instant)

object Comment {
  val tupled = (apply _).tupled

  implicit val writes: Writes[Comment] = Json.writes[Comment]

  def fromForm(params: (UUID, Int, String)) = apply(None, params._1, params._2, params._3, Instant.now)
}
