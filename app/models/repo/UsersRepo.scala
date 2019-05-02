package models.repo

import java.util.UUID
import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.User

@Singleton
class UsersRepo @Inject() (
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  val query = TableQuery[UsersTable]

  def findByID(id: UUID): Future[Option[User]] =
    db.run(query.filter(_.id === id).result.headOption)

  def findByEmail(email: String): Future[Option[User]] =
    db.run(query.filter(_.email.toUpperCase === email.toUpperCase).result.headOption)

  def insert(user: User): Future[Int] = db.run( query += user)

  def update(user_id: UUID, params: (String, String, String)): Future[Int] =
    db.run {
      query
        .filter(r => r.id === user_id)
        .map(r => (r.hashedPassword, r.profile, r.email))
        .update(params)
    }

  def userDetailInfo(user_id: UUID) :Future[User] =
    db.run(query.filter(_.id === user_id).result.head)

  class UsersTable(tag: Tag) extends Table[User](tag,"USERS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def email = column[String]("EMAIL")
    def hashedPassword = column[String]("HASHED_PASSWORD")
    def totp = column[Option[String]]("TOTP")
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")
    def birthday = column[java.sql.Date]("BIRTHDAY")
    def major = column[String]("MAJOR")
    def year = column[Int]("YEAR")
    def profile = column[String]("PROFILE")
    def adminFlag = column[Boolean]("ADMIN_FLAG")

    def * = (
      id,
      email,
      hashedPassword,
      totp,
      firstName,
      lastName,
      birthday,
      major,
      year,
      profile.?,
      adminFlag) <> (User.tupled, User.unapply)

    def idxEmail = index("IDX_EMAIL", email.toUpperCase, unique = true)
  }
}
