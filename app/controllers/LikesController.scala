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

    val likesForm = Form(
      "userID" -> uuid
    )

    def getPostLikesCount(post_id: Int) = Action.async {
      likesRepo.getPostLikesCount(post_id).map(n => Ok(Json.toJson(n)))
    }

    def getCommentLikesCount(comment_id: Int) = Action.async {
      likesRepo.getCommentLikesCount(comment_id).map(n => Ok(Json.toJson(n)))
    }

    def createPostLike(post_id: Int) = userAction.async { implicit request =>
      likesForm.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        { case (userID) =>
          likesRepo.likeAdd(Like.postFromForm(userID, Option(post_id)))
                   .map(n => if(n == 1) Ok else InternalServerError)
        }
      )
    }

    def createCommentLike(comment_id: Int) = userAction.async { implicit request =>
      likesForm.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        { case (userID) =>
          likesRepo.likeAdd(Like.commentFromForm(userID, Option(comment_id)))
                   .map(n => if(n == 1) Ok else InternalServerError)
        }
      )
    }

    def deletePostLike(post_id: Int) = userAction.async { implicit request =>
      likesRepo.likePostDelete(post_id).map(n => if(n == 1) Ok else InternalServerError)
    }

    def deleteCommentLike(comment_id: Int) = userAction.async { implicit request =>
      likesRepo.likeCommentDelete(comment_id).map(n => if(n == 1) Ok else InternalServerError)
    }
  }
