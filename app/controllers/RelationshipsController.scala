package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import utils.UserRequest

@Singleton
class RelationshipsController @Inject()(
    userAction: utils.UserAction,
    components: ControllerComponents,
    val usersRepo: models.repo.UsersRepo,
    implicit val ec: ExecutionContext)
  extends AbstractController(components)
  with play.api.i18n.I18nSupport {

  }
