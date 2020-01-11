package controllers

import controllers.forms.BookUpdate
import javax.inject.{Inject, Singleton}
import models.repositories.BookRepository
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class UpdateBookController @Inject() (cc: ControllerComponents, bookRepository: BookRepository)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def index(id: String): Action[AnyContent] = Action.async { implicit request =>
    bookRepository
      .findById(id)
      .map {
        case Some(book) => {
          val editForm = BookUpdate.form.fill(BookUpdate(book))
          Ok(views.html.book.update(editForm))
        }
        case None => NotFound("idが見つかりませんでした。")
      }
      .recover {
        case NonFatal(ex) => {
          logger.error(s"occurred error", ex)
          InternalServerError(ex.getMessage)
        }
      }
  }

  def update(): Action[AnyContent] = Action.async { implicit request =>
    BookUpdate.form.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.book.update(error))),
      updatingBook => {
        val book = updatingBook.toBookModel
        bookRepository
          .update(book)
          .map { _ =>
            Redirect("/books")
          }
          .recover {
            case NonFatal(ex) => {
              logger.error(s"occurred error", ex)
              InternalServerError(ex.getMessage)
            }
          }
      }
    )
  }
}
