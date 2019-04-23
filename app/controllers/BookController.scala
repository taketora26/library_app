package controllers

import controllers.dtos.BookDTO
import javax.inject.{Inject, Singleton}
import models.repositories.BookRepository
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.util.control.NonFatal

@Singleton
class BookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)
    extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def index(): Action[AnyContent] = Action { implicit request =>
    bookRepository
      .findAll()
      .map { books =>
        val bookDTOs = books.map(BookDTO(_))
        Ok(views.html.book.index(bookDTOs))
      }
      .recover {
        case NonFatal(ex) =>
          logger.error(s"occurred error", ex)
          InternalServerError(ex.getMessage)
      }
      .getOrElse(InternalServerError)
  }
}
