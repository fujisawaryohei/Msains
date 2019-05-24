package models.repo

import java.util.UUID
import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.Comment

@Singleton
class CommentsRepo @Inject() (
    val usersRepo: UsersRepo,
    val postsRepo: PostsRepo,
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  private val query = TableQuery[CommentsTable]

  def getTimelineComment(post_id: Int): Future[Seq[Comment]] = db.run {
    query.filter(_.id === post_id).result
  }

  def createTimelineComment(comment: Comment): Future[Int] = db.run(query += comment)

  def updateTimelineComment(comment_id: Int, content: (UUID, Int, String)): Future[Int] = db.run {
    query.filter(_.id === comment_id)
         .map(r => (r.userID, r.postID, r.content))
         .update(content)
  }

  def deleteTimelineComment(comment_id: Int): Future[Int] = db.run {
    query.filter(_.id === comment_id).delete
  }

  class CommentsTable(tag: Tag) extends Table[Comment](tag,"COMMENTS") {
    def id = column[Int]("ID", O.PrimaryKey)
    def userID = column[UUID]("USER_ID")
    def postID = column[Int]("POST_ID")
    def content = column[String]("CONTENT")
    def likeFlag = column[Boolean]("LIKE_FLAG")
    def createdAt = column[Instant]("CREATED_AT")

    def * = (
      id.?,
      userID,
      postID,
      content,
      likeFlag.?,
      createdAt) <> (Comment.tupled, Comment.unapply)

    def user = foreignKey("USERS_FK", userID, TableQuery[usersRepo.UsersTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)

    def post = foreignKey("POSTS_FK", postID, TableQuery[postsRepo.PostsTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)
  }
}
