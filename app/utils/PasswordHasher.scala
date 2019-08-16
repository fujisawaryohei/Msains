package utils

object PasswordHasher {
  private val saltLength: Int = 32
  private val hashLength: Int = 32
  private val iteration: Int = 20000

  private def parse(target: String): Option[(Array[Byte], Array[Byte])] = {
    def fromHex(hex: String): Array[Byte] =
      hex.sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    scala.util.Try {
      target.split(':') match {
        case Array(salt, hash) => (fromHex(salt), fromHex(hash))
        case _ => throw new IllegalArgumentException(s"Illegal credential format.")
      }
    }.toOption
  }

  def generateRawHash(credential: String, salt: Array[Byte]): Array[Byte] = {
    javax.crypto.SecretKeyFactory
      .getInstance("PBKDF2WithHmacSHA512")
      .generateSecret(new javax.crypto.spec.PBEKeySpec(
        credential.toCharArray, salt, iteration, hashLength * 8))
      .getEncoded()
  }

  def generate(credential: String): String = {
    def toHex(bytes: Array[Byte]): String =
      bytes.map("%02X".format(_)).mkString.toUpperCase

    val salt = Array.ofDim[Byte](saltLength)
    java.security.SecureRandom.getInstance("NativePRNGNonBlocking").nextBytes(salt)

    s"${toHex(salt)}:${toHex(generateRawHash(credential, salt))}"
  }

  def verify(credential: String, target: String): Option[Boolean] =
    parse(target).map { case (salt, tHash) =>
      java.util.Arrays.equals(generateRawHash(credential, salt), tHash)
    }
}
