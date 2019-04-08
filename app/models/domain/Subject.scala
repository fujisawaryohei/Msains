package models.domain

import java.time.Instant

case class Subject(
    id: Int,
    nam: String,
    teacherName: String,
    roomNumber: Int,
    unit: Int,
    weekCode: Int,
    startPeriod: Instant,
    endPeriod: Instant)
