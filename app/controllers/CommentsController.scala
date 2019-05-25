package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import models.domain.Comment
import models.domain.Post
import utils.UserRequest
import java.io.File

@Singleton
class CommentsController @Inject()(
  userAction: utils.UserAction,
  components: ControllerComponents,
  val commentsRepo: models.repo.CommentsRepo,
  implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

    val commentParamstuple = tuple(
      "userID" -> uuid,
      "postID" -> number,
      "content" -> nonEmptyText(maxLength=255)
    )

    val commentParamsMapping = Form(
      "params" -> commentParamstuple
    )

    val commentUpdateParamsMapping = Form(commentParamstuple)

    def getComments(post_id: Int) = Action.async {
      commentsRepo.getTimelineComment(post_id).map(n => Ok(Json.toJson(n)))
    }

    def commentCreate = userAction.async { implicit request =>
      commentParamsMapping.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        { case (params) =>
          commentsRepo.createTimelineComment(Comment.fromForm(params))
          .map(n => if(n == 1) Ok else InternalServerError)
        }
      )
    }

    def commentUpdate(comment_id: Int) = userAction.async { implicit request =>
      commentUpdateParamsMapping.bindFromRequest.fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        { case (contentParam) =>
          commentsRepo.updateTimelineComment(comment_id, contentParam)
          .map(n => if(n == 1) Ok else InternalServerError)
        }
      )
    }

    def commentDelete(comment_id: Int) = userAction.async {
      commentsRepo.deleteTimelineComment(comment_id).map(n => if(n == 1) Ok else InternalServerError)
    }
  }
