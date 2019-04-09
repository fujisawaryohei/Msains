package models.repo

import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.CommentReply
import models.domain.Comment

@Singleton
class CommentRepliesRepo @Inject() (
    val commetsRepo: CommentRepo,
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  class CommentRepliesTable(tag: Tag) extends Table[CommentReply](tag,"COMMENT_REPLIES") {
    def id = column[Int]("COMMENTREPLIY_ID", O.PrimaryKey)
    def commentID = column[Int]("COMMENT_ID", O.PrimaryKey)
    def content = column[String]("CONTENT")
    def createdAt = column[Instant]("CREATED_AT")

    def * = (
      id,
      commentID,
      content,
      createdAt) <> (CommentReply.tupled, CommentReply.unapply)

    def comment = foreignKey("COMMETS_FK", commentID, TableQuery[commetsRepo.CommentTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)
  }
}
