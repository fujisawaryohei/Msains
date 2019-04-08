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
    imageURL: String,
    postType: String,
    createdAt: Instant)
