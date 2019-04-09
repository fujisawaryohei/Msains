package models.repo

import java.util.UUID
import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.Post

@Singleton
class PostsRepo @Inject() (
    val usersRepo: UsersRepo,
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  private val query = TableQuery[PostsTable]

  def getThreads: Future[Seq[Post]] =
    db.run(query.filter(_.postType === "Thread").result)

  def getTimelines: Future[Seq[Post]] =
    db.run(query.filter(_.postType === "Timeline").result)

  def getUserTimelines(user_id: UUID): Future[Seq[Post]] =
    db.run(query.filter(_.userID === user_id).filter(_.postType === "Timeline").result)

  def getUserThreads(user_id: UUID): Future[Seq[Post]] =
    db.run(query.filter(_.userID === user_id).filter(_.postType === "Threads").result)

  def getDetailResult(id: Int): Future[Post] =
    db.run(query.filter(_.id === id).result.head)

  def add(post: Post): Future[Int] =
    db.run(query += post)

  def update(userID: UUID, id: Int, params: (String, String, String)): Future[Int] = db.run {
    query
      .filter(r => r.userID === userID && r.id === id)
      .map(r => (r.title, r.content, r.imageURL))
      .update(params)
  }

  def delete(userID: UUID, id: Int): Future[Int] =
    db.run(query.filter(r => r.userID === userID && r.id === id).delete)

  class PostsTable(tag: Tag) extends Table[Post](tag,"POSTS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def userID = column[UUID]("USER_ID")
    def title = column[String]("TITLE")
    def content = column[String]("CONTENT")
    def imageURL = column[String]("IMAGE_URL")
    def postType = column[String]("TYPE")
    def createdAt = column[Instant]("CREATED_AT")

    def * = (
        id.?,
        userID,
        title,
        content,
        imageURL,
        postType,
        createdAt) <> (Post.tupled, Post.unapply)

    def pk = primaryKey("PK_POSTS", (id, userID))

    def user = foreignKey("USERS_FK", userID, TableQuery[usersRepo.UsersTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)
  }
}
