package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import models.repo.LikesRepo
import models.domain.Like
import utils.UserRequest

@Singleton
class LikesController @Inject()(
    userAction: utils.UserAction,
    components: ControllerComponents,
    val likesRepo: LikesRepo,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

    val likesPamamsMapping = Form(
      mapping(
        "id" -> optional(number),
        "userID" -> uuid,
        "postID" -> optional(number),
        "commentID" -> optional(number)
      )(Like.apply)(Like.unapply)
    )

    def getPostLikesCount(id: Int) = Action.async {
      likesRepo.getPostLikesCount(id).map(n => Ok(Json.toJson(n)))
    }

    def getCommentLikesCount(id: Int) = Action.async {
      likesRepo.getCommentLikesCount(id).map(n => Ok(Json.toJson(n)))
    }

    def createLike(id: Int) = Action.async { implicit request =>
      likesPamamsMapping.bindFromRequest.fold(
        error =>
          Future.successful(BadRequest(error.errorsAsJson)),
        value =>
          likesRepo.likeAddComment(value).map(n => if(n == 1) Ok else InternalServerError)
      )
    }
  }
