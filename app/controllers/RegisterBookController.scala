package controllers

import controllers.forms.BookRegister
import javax.inject.{Inject, Singleton}
import models.Book
import models.repositories.BookRepository
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.util.{Failure, Success}

@Singleton
class RegisterBookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)
    extends AbstractController(cc)
    with I18nSupport {

  def index(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.book.register(BookRegister.form))
  }

  def register() = Action { implicit request =>
    BookRegister.form.bindFromRequest.fold(
      error => BadRequest(views.html.book.register(error)),
      book => {
        bookRepository
          .findByName(book.name)
          .map {
            case Nil =>
              bookRepository.add(Book(book.name, book.author, book.publishedDate, book.description)) match {
                case Success(_)  => Redirect("/books")
                case Failure(ex) => InternalServerError(ex.getMessage)
              }

            case _ => Redirect("/books")
          }
          .getOrElse(InternalServerError)
      }
    )
  }
}
