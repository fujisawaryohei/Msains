package models.domain

import ejisan.kuro.otp._

case class User(
    id: java.util.UUID,
    email: String,
    hashedPassword: String,
    totp: Option[String],
    firstName: String,
    lastName: String,
    birthday: java.time.Instant,
    major: String,
    profile: String,
    adminFlag: Boolean)
