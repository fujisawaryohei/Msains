package models.domain

import java.util.UUID
import java.time.Instant
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json.Writes

case class Relationship(
    followingID: UUID,
    followerID: UUID,
    createdAt: java.time.Instant)

object Relationship {
    val tupled = (apply _).tupled

    implicit val writes: Writes[Relationship] = Json.writes[Relationship]

    def fromForm(followingID: UUID, followerID: UUID): Relationship = apply(followingID, followerID, Instant.now)
}
