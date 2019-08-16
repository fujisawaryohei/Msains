package models.domain

import ejisan.kuro.otp._
import utils.PasswordHasher
import java.util.UUID
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json.Writes

case class User(
    id: java.util.UUID,
    email: String,
    hashedPassword: String,
    totp: Option[String],
    firstName: String,
    lastName: String,
    birthday: java.sql.Date,
    major: String,
    year: Int,
    profile: Option[String],
    adminFlag: Boolean) {

  def verifyPassword(password: String): Boolean =
    utils.PasswordHasher.verify(password, hashedPassword).getOrElse(false)

  def verifyTOTPCode(code: Option[String]): Boolean = (totp, code) match {
    case (Some(key), Some(c)) => {
      // 6 digits, 30 secs => Google Authenticator default
      val totp = TOTP(OTPAlgorithm.SHA256, 6, 30, OTPKey.fromBase32(key))
      // 1 means accept 30 secs before
      totp.validate(1, c)
    }
    case (None, None) => true
    case _ => false
  }

  def verifyCredentials(password: String, code: Option[String]): Boolean =
    verifyPassword(password) && verifyTOTPCode(code)
}

object User {
  val tupled = (apply _).tupled

  implicit val writes: Writes[User] = Json.writes[User]

  def fromForm(
      params: (String, String, String, java.sql.Date, String, Int),
      email: String
      ) = apply(
          UUID.randomUUID(),
          email,
          PasswordHasher.generate(params._1),
          None,
          params._2,
          params._3,
          params._4,
          params._5,
          params._6,
          None,
          false
        )
}
