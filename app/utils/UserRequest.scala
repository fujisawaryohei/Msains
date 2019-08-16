package utils

import play.api.mvc.{ Request, WrappedRequest }

class UserRequest[A](
    request: Request[A],
    val user: models.domain.User)
  extends WrappedRequest[A](request) {
  def userID: java.util.UUID = user.id
}
