package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import utils.UserRequest
import models.domain.Relationship
import models.repo.UsersRepo
import models.repo.RelationshipsRepo

@Singleton
class RelationshipsController @Inject()(
    userAction: utils.UserAction,
    components: ControllerComponents,
    val usersRepo: models.repo.UsersRepo,
    val relationshipsRepo: models.repo.RelationshipsRepo,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

    def relationParamsMapping = Form(
      "loginUserID" -> uuid
    )

    def following(user_id: UUID) = userAction.async { implicit request => 
      relationParamsMapping.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        (loginUserID) => {
          relationshipsRepo.add(Relationship.fromForm(user_id, loginUserID))
          .map(n => if(n == 1) Ok("following") else InternalServerError)
        }
      )
    }

    def unfollowing(user_id: UUID) = userAction.async { implicit request =>
      relationParamsMapping.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        (loginUserID) => {
          relationshipsRepo.delete(user_id, loginUserID)
          .map(n => if (n == 1) Ok("unfollowing") else InternalServerError)
        }
      )
    }
  }
