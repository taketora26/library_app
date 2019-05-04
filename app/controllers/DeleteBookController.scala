package controllers

import javax.inject.{Inject, Singleton}
import models.repositories.BookRepository
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents, _}
import scala.util.{Failure, Success}

@Singleton
class DeleteBookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)
    extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def delete(bookId: String): Action[AnyContent] = Action { implicit request =>
    bookRepository.delete(bookId) match {
      case Success(_) => Redirect("/books")
      case Failure(ex) => {
        logger.error(s"occurred error", ex)
        Redirect("/books")
      }
    }
  }
}
