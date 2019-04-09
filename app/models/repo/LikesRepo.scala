package models.repo

import java.util.UUID
import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.Like
import models.repo._

@Singleton
class LikesRepo @Inject() (
    val usersRepo: UsersRepo,
    val postsRepo: PostsRepo,
    val commentsRepo: CommentRepo,
    val commentsRepliesRepo: CommentRepliesRepo,
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  class LikesTable(tag: Tag) extends Table[Like](tag, "LIKES") {
    def id = column[Int] ("ID",O.PrimaryKey, O.AutoInc)
    def userID = column[UUID]("USER_ID")
    def postID = column[Int]("POST_ID")
    def commentID = column[Int]("COMMENT_ID")
    def commentReplyID = column[Int]("COMMENTREPLY_ID")
    def createdAt = column[Instant]("CREATEDAT")

    def user = foreignKey("USER_FK", userID, TableQuery[usersRepo.UsersTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)

    def post = foreignKey("POST_FK", postID, TableQuery[postsRepo.PostsTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)

    def comment = foreignKey("COMMENT_FK", commentID, TableQuery[commentsRepo.CommentTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade
    )

    def commentReply = foreignKey("COMMENTREPLY_FK", commentReplyID, TableQuery[commentsRepliesRepo.CommentRepliesTable])(
      _.commentID,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade
    )

    def * = (
        id,
        userID,
        postID,
        commentID,
        commentReplyID,
        createdAt) <> (Like.tupled, Like.unapply)

    def pk = primaryKey("PK_LIKES", id)
  }
}
