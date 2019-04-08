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
class PostsController @Inject()(
    userAction: utils.UserAction,
    components: ControllerComponents,
    val postsRepo: models.repo.PostsRepo,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

  val postParamMapping = tuple(
    "title" -> nonEmptyText(maxLength = 10),
    "content" -> nonEmptyText(maxLength = 255),
    "image_url" -> nonEmptyText(maxLength = 255))

  val postAddtionForm = Form(tuple(
    "params" -> postParamMapping,
    "post_type" -> text.verifying(
      "Wrong type given",
      t => t == "TIMELINE" && t == "THREAD")
  ))

  val postUpdateForm = Form(postParamMapping)

  def getThreads = Action.async {
    postsRepo.getThreads.map(threads => Ok(Json.toJson(threads)))
  }

  def getTimelines = Action.async {
    postsRepo.getThreads.map(timelines => Ok(Json.toJson(timelines)))
  }

  def getDetailResult(post_id: Int) = Action.async {
    postsRepo.getDetailsResult(post_id).map(result => Ok(Json.toJson(result)))
  }

  def add = userAction.async { implicit request =>
    postAddtionForm.bindFromRequest.fold(
      error =>
        Future.successful(BadRequest(error.errorsAsJson)),
      { case (params, postType) =>
        postsRepo
          .add(Post.fromForm(request.userID, params, postType))
          .map { n =>
            if (n == 1) Ok else InternalServerError
          }
      })
  }

  def update(id: Int) = userAction.async { implicit request =>
    postUpdateForm.bindFromRequest.fold(
      error =>
        Future.successful(BadRequest(error.errorsAsJson)),
      postsRepo.update(request.userID, id, _).map { n =>
        if (n == 1) Ok else InternalServerError
      })
  }

  def delete(id: Int) = userAction.async { implicit request =>
    postsRepo.delete(request.userID, id).map { n =>
      if (n == 1) Ok else InternalServerError
    }
  }
}
