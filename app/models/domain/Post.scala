package models.domain

import java.util.UUID
import java.time.Instant
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json.Writes

case class Post(
    id: Option[Int],
    userID: UUID,
    title: String,
    content: String,
    filename: String,
    postType: String,
    createdAt: Instant)

object Post {
  val tupled = (apply _).tupled

  implicit val writes: Writes[Post] = Json.writes[Post]

  def fromForm(
      userID: UUID,
      params: (String, String, String),
      postType: String): Post =
    apply(None, userID, params._1, params._2, params._3, postType, Instant.now)
}

sealed class PostType(val typeName: String)

object PostType {
  case object Thread extends PostType("THREAD")
  case object Timeline extends PostType("TIMELINE")
}
