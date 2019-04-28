package controllers

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import models.domain.User
import utils.UserRequest
import scala.util.matching.Regex

@Singleton
class AuthenticationController @Inject()(
    components: ControllerComponents,
    val usersRepo: models.repo.UsersRepo,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

  val userParamMapping = tuple(
    "hashedPassword" -> nonEmptyText(minLength=6,maxLength=20),
    "firstName" -> nonEmptyText(minLength=1,maxLength=10),
    "lastName" -> nonEmptyText(minLength=1,maxLength=10),
    "birthday" -> sqlDate,
    "major" -> nonEmptyText,
    "year" -> number(min=1,max=4))

  val signUpForm = Form(tuple(
    "params" -> userParamMapping,
    "email" -> text.verifying(
      "your address can't use..",
      t =>  {
        val mailPattern = "@seinan-gakuin.jp".r
        if(mailPattern.findFirstIn(t).nonEmpty) true else false
      }
    )
  ))

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

  def signUp = Action.async { implicit request =>
    signUpForm.bindFromRequest.fold(
      error =>
        Future.successful(BadRequest(error.errorsAsJson)),
      { case (params,email) =>
        usersRepo
          .insert(User.fromForm(params,email))
          .map { n =>
            if (n == 1) Ok else InternalServerError
          }
      })
  }

  def signOut = Action {
    Ok.withNewSession
  }
}
