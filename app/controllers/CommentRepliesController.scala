package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import models.domain.CommentReply
import utils.UserRequest

@Singleton
class CommentRepliesController @Inject()(
  userAction: utils.UserAction,
  components: ControllerComponents,
  val commentRepliesRepo: models.repo.CommentRepliesRepo,
  implicit val ec: ExecutionContext)
  extends AbstractController(components) 
  with play.api.i18n.I18nSupport {
    val commentRepForm = Form(tuple(
      "userID" -> uuid,
      "content" -> text(maxLength = 255)
      )
    )
  }