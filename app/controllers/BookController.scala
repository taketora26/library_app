package controllers

import controllers.dtos.BookDTO
import controllers.forms.BookSearch
import javax.inject.{Inject, Singleton}
import models.repositories.{BookRepository, Context}
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.util.{Failure, Success}

@Singleton
class BookController @Inject() (cc: ControllerComponents, bookRepository: BookRepository, implicit val ctx: Context)
    extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def index(): Action[AnyContent] =
    Action { implicit request =>
      bookRepository.findAll() match {
        case Success(books) => {
          val bookDTOs = books.map(BookDTO(_))
          Ok(views.html.book.index(bookDTOs))
        }
        case Failure(ex) => {
          logger.error(s"index occurred error", ex)
          InternalServerError(ex.getMessage)
        }
      }
    }

  def search(): Action[AnyContent] =
    Action { implicit request =>
      BookSearch.form.bindFromRequest.fold(
        _ => Redirect("/books"),
        bookName => {
          bookRepository
            .searchName(bookName.name) match {
            case Success(books) => {
              val bookDTOs = books.map(BookDTO(_))
              Ok(views.html.book.index(bookDTOs))
            }
            case Failure(ex) => {
              logger.error(s"occurred error", ex)
              InternalServerError(ex.getMessage)
            }
          }
        }
      )
    }
}
