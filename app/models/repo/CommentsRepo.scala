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

  class CommentTable(tag: Tag) extends Table[Comment](tag,"COMMENTS") {
    def id = column[Int]("ID", O.PrimaryKey)
    def userID = column[UUID]("USER_ID")
    def postID = column[Int]("POST_ID")
    def content = column[String]("CONTENT")
    def likeFlag = column[Boolean]("LIKE_FLAG")
    def createdAt = column[Instant]("CREATED_AT")

    def * = (
      id,
      userID,
      postID,
      content,
      likeFlag,
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
