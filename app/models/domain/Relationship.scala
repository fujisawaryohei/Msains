package models.domain

import java.util.UUID

case class Relationship(
    followeeID: UUID,
    followerID: UUID,
    createdAt: java.time.Instant)
