package utils

import java.util.UUID
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import models.domain.User

@Singleton
class UserAction @Inject() (
    val parser: BodyParser[AnyContent],
    val userRepo: models.repo.UsersRepo)(
    implicit
    val executionContext: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] {

  def onUnauthorized(implicit request: RequestHeader): Result =
    Results.Unauthorized.withNewSession

  def invokeBlock[A](
      request: Request[A],
      block: (UserRequest[A]) => Future[Result]): Future[Result] =
    authenticate(request, block)

  def authenticate[A](
      request: Request[A],
      block: (UserRequest[A]) => Future[Result]): Future[Result] = {
    request.session
      .get("id")
      .flatMap(id => scala.util.Try(UUID.fromString(id)).toOption)
      .map { id =>
        userRepo.findByID(id).flatMap {
          case Some(user) =>
            block(new UserRequest(request, user))
              .map(_.withSession("id" -> id.toString))
          case None =>
            Future.successful(onUnauthorized(request))
        }
      } getOrElse {
        Future.successful(onUnauthorized(request))
      }
  }
}
