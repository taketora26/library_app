package controllers

import controllers.forms.BookUpdate
import javax.inject.{Inject, Singleton}
import models.repositories.{BookRepository, Context}
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.util.{Failure, Success}

@Singleton
class UpdateBookController @Inject() (
    cc: ControllerComponents,
    bookRepository: BookRepository,
    implicit val ctx: Context
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def index(id: String): Action[AnyContent] =
    Action { implicit request =>
      bookRepository.findById(id) match {
        case Success(Some(book)) => {
          val editForm = BookUpdate.form.fill(BookUpdate(book))
          Ok(views.html.book.update(editForm))
        }
        case Success(None) => NotFound("idが見つかりませんでした。")
        case Failure(ex) => {
          logger.error(s"occurred error", ex)
          InternalServerError(ex.getMessage)
        }
      }
    }

  def update(): Action[AnyContent] =
    Action { implicit request =>
      BookUpdate.form.bindFromRequest.fold(
        error => BadRequest(views.html.book.update(error)),
        updatingBook => {
          val book = updatingBook.toBookModel
          bookRepository.update(book) match {
            case Success(_) => Redirect("/books")
            case Failure(ex) => {
              logger.error(s"occurred error", ex)
              InternalServerError(ex.getMessage)
            }
          }
        }
      )
    }
}
