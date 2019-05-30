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

    val likesPostParamstuple = tuple(
      "userID" -> uuid,
      "commentID" -> optional(number)
    )

    val likesCommentParamstuple = tuple(
      "userID" -> uuid,
      "postID" -> optional(number)
    )

    val likesPostParamsMapping = Form(
      "params" -> likesPostParamstuple
    )

    val likesCommentParamsMapping = Form(
      "params" -> likesCommentParamstuple
    )

    def getPostLikesCount(id: Int) = Action.async {
      likesRepo.getPostLikesCount(id).map(n => Ok(Json.toJson(n)))
    }

    def getCommentLikesCount(id: Int) = Action.async {
      likesRepo.getCommentLikesCount(id).map(n => Ok(Json.toJson(n)))
    }

    def createPostLike(id: Option[Int]) = Action.async { implicit request =>
      likesPostParamsMapping.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        { case (params) =>
          likesRepo.likeAdd(Like.postFromForm(id,params))
                   .map(n => if(n == 1) Ok else InternalServerError)
        }
      )
    }

    def createCommentLike(id: Option[Int]) = Action.async { implicit request =>
      likesCommentParamsMapping.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        { case (params) =>
          likesRepo.likeAdd(Like.commentFromForm(id,params))
                   .map(n => if(n == 1) Ok else InternalServerError)
        }
      )
    }
  }
