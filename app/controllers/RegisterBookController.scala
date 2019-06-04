package controllers

import controllers.forms.BookRegister
import javax.inject.{Inject, Singleton}
import models.Book
import models.exception.DuplicateBookNameException
import models.repositories.{BookRepository, Context}
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.util.{Failure, Success}

@Singleton
class RegisterBookController @Inject()(cc: ControllerComponents,
                                       bookRepository: BookRepository,
                                       implicit val ctx: Context)
    extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def index(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.book.register(BookRegister.form))
  }

  def register() = Action { implicit request =>
    BookRegister.form.bindFromRequest.fold(
      error => BadRequest(views.html.book.register(error)),
      book => {
        (for {
          books  <- bookRepository.findByName(book.name)
          _      <- Book.canRegister(books)
          result <- bookRepository.add(Book(book.name, book.author, book.publishedDate, book.description))
        } yield result) match {
          case Success(_) => Redirect("/books")

          case Failure(ex: DuplicateBookNameException) =>
            BadRequest(views.html.book.register(BookRegister.form.withGlobalError(ex.getMessage)))
          case Failure(ex) => {
            logger.error(s"occurred error", ex)
            InternalServerError(ex.getMessage)
          }
        }
      }
    )
  }
}
