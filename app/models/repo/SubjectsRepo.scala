package models.repo

import java.time.Instant
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }
import models.domain.Subject

@Singleton
class SubjectsRepo @Inject() (
    dbConfigProvider: play.api.db.slick.DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  import dbConfig._
  import profile.api._

  class SubjectsTable(tag: Tag) extends Table[Subject](tag,"SUBJECTS"){
    def id = column[Int]("ID", O.PrimaryKey)
    def name= column[String]("NAME")
    def teacherName = column[String]("TEACHER_NAME")
    def roomNumber = column[Int]("ROOM_NUMBER")
    def unit = column[Int]("UNIT")
    def weekCode = column[Int]("WEEK_CODE")
    def startPeriod = column[Instant]("START_PERIOD")
    def endPeriod = column[Instant]("END_PERIOD")

    def * = (
      id,
      name,
      teacherName,
      roomNumber,
      unit,
      weekCode,
      startPeriod,
      endPeriod) <> (Subject.tupled, Subject.unapply)
  }
}
