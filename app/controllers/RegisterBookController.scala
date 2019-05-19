package controllers

import controllers.forms.BookRegister
import javax.inject.{Inject, Singleton}
import models.Book
import models.exception.{DuplicateBookNameError, RegisterError}
import models.repositories.BookRepository
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class RegisterBookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def index(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.book.register(BookRegister.form))
  }

  def register() = Action.async { implicit request =>
    BookRegister.form.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.book.register(error))),
      book => {
        val xx: Future[Either[RegisterError, Unit]] = for {
          books <- bookRepository.findByName(book.name)
        } yield
          for {
            _ <- Book.canRegister(books)
            _ <- bookRepository.add(Book(book.name, book.author, book.publishedDate, book.description)).toEither
          } yield ()
        xx.map {
            case Right(_) => Redirect("/books")
            case Left(DuplicateBookNameError) =>
              BadRequest(views.html.book.register(BookRegister.form.withGlobalError(DuplicateBookNameError.message)))
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
