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
