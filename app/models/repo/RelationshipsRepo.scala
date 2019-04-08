package models.repo

import java.util.UUID
import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.Relationship

@Singleton
class RelationshipsRepo @Inject() (
    val usersRepo: UsersRepo,
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  class RelationshipsTable(tag: Tag) extends Table[Relationship](tag,"RELATIONSHIPS") {
    def followingID = column[UUID]("FOLLOWING_ID")
    def followerID = column[UUID]("FOLLOWER_ID")
    def createdAt = column[Instant]("CREATED_AT")

    def following = foreignKey("FOLLOWING_FK", followingID, TableQuery[usersRepo.UsersTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)

    def follower = foreignKey("FOLLOWER_FK", followerID, TableQuery[usersRepo.UsersTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)

    def * = (
        followingID,
        followerID,
        createdAt) <> (Relationship.tupled, Relationship.unapply)

    def pk = primaryKey("PK_RELATIONSHIPS", (followingID, followerID))
  }
}
