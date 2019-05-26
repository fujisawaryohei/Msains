package models.repo

import java.util.UUID
import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.Like
import java.util.UUID

@Singleton
class LikesRepo @Inject() (
  val usersRepo: UsersRepo,
  val postsRepo: PostsRepo,
  val commentsRepo: CommentsRepo,
  dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
  implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

    import dbConfig._
    import profile.api._

    val query = TableQuery[LikesTable]

    def getPostLikesCount(post_id: Int): Future[Seq[Like]] =
      db.run(query.filter(_.id === post_id).result)

    def getCommentLikesCount(comment_id: Int): Future[Seq[Like]] =
      db.run(query.filter(_.id === comment_id).result)

    class LikesTable(tag: Tag) extends Table[Like](tag, "LIKES"){
      def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
      def userID = column[UUID]("USER_ID")
      def postID = column[Int]("POST_ID")
      def commentID = column[Int]("COMMENT_ID")

      def * = (
        id.?,
        userID,
        postID.?,
        commentID.?) <> (Like.tupled, Like.unapply)

      def user = foreignKey("USER_ID", userID, TableQuery[usersRepo.UsersTable])(
        _.id,
        onUpdate = ForeignKeyAction.Cascade,
        onDelete = ForeignKeyAction.Cascade)
      def post = foreignKey("POST_ID", postID, TableQuery[postsRepo.PostsTable])(
        _.id,
        onUpdate = ForeignKeyAction.Cascade,
        onDelete = ForeignKeyAction.Cascade)
      def comment = foreignKey("COMMENT_ID", commentID, TableQuery[commentsRepo.CommentsTable])(
        _.id,
        onUpdate = ForeignKeyAction.Cascade,
        onDelete = ForeignKeyAction.Cascade)
    }
  }
