package controllers

import javax.inject.{Inject, Singleton}
import models.repositories.BookRepository
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents, _}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

@Singleton
class DeleteBookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def delete(bookId: String): Action[AnyContent] = Action.async { implicit request =>
    bookRepository
      .delete(bookId)
      .map { _ =>
        Redirect("/books")
      }
      .recover {
        case NonFatal(e) => {
          logger.error(s"occurred error", e)
          Redirect("/books")
        }
      }
  }
}
