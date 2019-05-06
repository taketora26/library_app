package controllers

import infra.rdb.ContextOnJDBC
import models.repositories.BookRepository
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, stubControllerComponents, _}

import scala.util.{Failure, Success}
class DeleteBookControllerSpec extends PlaySpec with MockitoSugar with Results {

  private val mockBookRepository   = mock[BookRepository]
  private implicit val mockContext = mock[ContextOnJDBC]

  private val controller =
    new DeleteBookController(
      stubControllerComponents(),
      mockBookRepository,
      mockContext
    )

  "delete(bookId)" should {
    "登録されている本を削除して、本の一覧にリダイレクトする" in {
      when(mockBookRepository.delete("book_id_1")).thenReturn(Success(()))
      val result = controller.delete("book_id_1").apply(FakeRequest().withCSRFToken)
      assert(status(result) === SEE_OTHER)
    }

    "BookRepository.deleteで例外が発生した場合に、本の一覧にリダイレクトする" in {
      when(mockBookRepository.delete("book_id_1")).thenReturn(Failure(new Exception("Something happened")))
      val result = controller.delete("book_id_1").apply(FakeRequest().withCSRFToken)
      assert(status(result) === SEE_OTHER)
    }
  }

}
