package controllers

import javax.inject.{ Inject, Singleton }
import scala.concurrent. { ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import models.domain.User
import utils.UserRequest

@Singleton
class UsersController @Inject()(
    userAction: utils.UserAction,
    components: ControllerComponents,
    val usersRepo: models.repo.UsersRepo,
    val user: models.domain.User,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

    val editParamMapping = tuple(
      "hashedPassword" -> nonEmptyText(minLength=6,maxLength=20),
      "profile" -> nonEmptyText(minLength=1,maxLength=255),
      "email" -> text.verifying("your address can't use..",
                   t => { val mailPattern = "@seinan-gakuin.jp".r
                   if(mailPattern.findFirstIn(t).nonEmpty) true else false }),
    )

    val editForm = Form(editParamMapping)

    def editInfo(user_id: java.util.UUID) = userAction.async{ implicit request =>
      editForm.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        { case (params) =>
          usersRepo
            .update(user_id, params)
            .map(n => if(n == 1) Ok else InternalServerError)
        }
      )
    }
  }
