package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import models.repo.LikesRepo
import utils.UserRequest

@Singleton
class LikesController @Inject()(
    userAction: utils.UserAction,
    components: ControllerComponents,
    val likesRepo: LikesRepo,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

    val likesParamstuple = tuple(
      "userID" -> uuid,
      "postID" -> number,
      "commentID" -> number)

    val likesPamamsMapping = Form(
      "params" -> likesParamstuple)

    def getPostLikesCount(id: Int) = Action.async {
      likesRepo.getPostLikesCount(id).map(n => Ok(Json.toJson(n)))
    }

    def getCommentLikesCount(id: Int) = Action.async {
      likesRepo.getCommentLikesCount(id).map(n => Ok(Json.toJson(n)))
    }
  }
