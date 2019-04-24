package controllers

import controllers.forms.BookUpdate
import javax.inject.{Inject, Singleton}
import models.repositories.BookRepository
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.util.{Failure, Success}

@Singleton
class UpdateBookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)
    extends AbstractController(cc)
    with I18nSupport {

  def index(id: String): Action[AnyContent] = Action { implicit request =>
    bookRepository.findById(id) match {
      case Failure(ex)   => InternalServerError(ex.getMessage)
      case Success(None) => NotFound("idが見つかりませんでした。")
      case Success(Some(book)) => {
        val editForm = BookUpdate.form.fill(BookUpdate(book))
        Ok(views.html.book.update(editForm))
      }
    }
  }
}
