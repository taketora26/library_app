package controllers

import controllers.dtos.BookDTO
import controllers.forms.BookSearch
import javax.inject.{Inject, Singleton}
import models.repositories.BookRepository
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class BookController @Inject() (cc: ControllerComponents, bookRepository: BookRepository)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    bookRepository
      .findAll()
      .map { books =>
        {
          val bookDTOs = books.map(BookDTO(_))
          Ok(views.html.book.index(bookDTOs))
        }
      }
      .recover {
        case NonFatal(e) => {
          logger.error(s"index occurred error", e)
          InternalServerError(e.getMessage)
        }
      }
  }

  def search(): Action[AnyContent] = Action.async { implicit request =>
    BookSearch.form.bindFromRequest.fold(
      _ => Future.successful(Redirect("/books")),
      bookName => {
        bookRepository
          .searchName(bookName.name)
          .map { books =>
            {
              val bookDTOs = books.map(BookDTO(_))
              Ok(views.html.book.index(bookDTOs))
            }
          }
          .recover {
            case NonFatal(e) => {
              logger.error(s"index occurred error", e)
              InternalServerError(e.getMessage)
            }
          }
      }
    )
  }
}
