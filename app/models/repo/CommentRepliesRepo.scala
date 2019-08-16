package models.repo

import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.CommentReply
import models.domain.Comment

@Singleton
class CommentRepliesRepo @Inject() (
    val commetsRepo: CommentsRepo,
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  class CommentRepliesTable(tag: Tag) extends Table[CommentReply](tag,"COMMENT_REPLIES") {
    def commentID = column[Int]("COMMENT_ID", O.PrimaryKey)
    def content = column[String]("CONTENT")
    def likeFlag = column[Boolean]("LIKE_FLAG")
    def createdAt = column[Instant]("CREATED_AT")

    def * = (
      commentID,
      content,
      likeFlag,
      createdAt) <> (CommentReply.tupled, CommentReply.unapply)

    def comment = foreignKey("COMMETS_FK", commentID, TableQuery[commetsRepo.CommentTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)
  }
}
