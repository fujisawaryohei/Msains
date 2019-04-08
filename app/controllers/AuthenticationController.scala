package controllers

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import models.domain.Post
import utils.UserRequest

@Singleton
class AuthenticationController @Inject()(
    components: ControllerComponents,
    val usersRepo: models.repo.UsersRepo,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

  val signInForm = Form(tuple(
    "email" -> email,
    "password" -> nonEmptyText,
    "code" -> optional(nonEmptyText)
  ))

  def signIn = Action.async { implicit request =>
    signInForm.bindFromRequest.fold(
      error =>
        Future.successful(BadRequest(error.errorsAsJson)),
      { case (email, password, code) =>
        usersRepo.findByEmail(email).map {
          case Some(user) =>
            if (user.verifyCredentials(password, code)) {
              Ok.withSession("id" -> user.id.toString)
            } else Unauthorized
          case None => Unauthorized
        }
      })
  }

  def signOut = Action {
    Ok.withNewSession
  }
}
