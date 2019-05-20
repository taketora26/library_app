package controllers

import controllers.forms.BookRegister
import infra.rdb.ExecutionContextOnJDBC
import javax.inject.{Inject, Singleton}
import models.Book
import models.exception.DuplicateBookNameException
import models.repositories.BookRepository
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import util.EitherOps._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class RegisterBookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)(
    implicit ec: ExecutionContextOnJDBC
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
        val result: Future[Unit] = for {
          books <- bookRepository.findByName(book.name)
          _     <- Book.canRegister(books).toFuture()
          _     <- bookRepository.add(Book(book.name, book.author, book.publishedDate, book.description))
        } yield ()
        result
          .map { _ =>
            Redirect("/books")
          }
          .recover {
            case e: DuplicateBookNameException =>
              BadRequest(views.html.book.register(BookRegister.form.withGlobalError(e.getMessage)))
            case NonFatal(ex) => {
              logger.error(s"occurred error", ex)
              InternalServerError(ex.getMessage)
            }
          }
      }
    )
  }
}
