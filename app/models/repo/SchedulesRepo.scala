package models.repo

import java.util.UUID
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.Schedule

@Singleton
class SchedulesRepo @Inject() (
    val usersRepo: UsersRepo,
    val subjectsRepo: SubjectsRepo,
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  class SchedulesTable(tag: Tag) extends Table[Schedule](tag,"SCHEDULES"){
    def userID = column[UUID]("USER_ID")
    def subjectID = column[Int]("SUBJECT_ID")

    def * = (userID, subjectID) <> (Schedule.tupled, Schedule.unapply)

    def pk = primaryKey("PK_SCHEDULES", (userID, subjectID))

    def user = foreignKey("FK_SCHEDULES_USER", userID, TableQuery[usersRepo.UsersTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)

    def subject = foreignKey("FK_SCHEDULES_SUBJECT", subjectID, TableQuery[subjectsRepo.SubjectsTable])(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)
  }
}
