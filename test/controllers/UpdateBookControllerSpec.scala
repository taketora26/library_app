package controllers

import java.time.LocalDate

import models._
import models.repositories.BookRepository
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, stubControllerComponents, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
class UpdateBookControllerSpec extends PlaySpec with MockitoSugar with Results {

  private val mockBookRepository = mock[BookRepository]

  private val controller =
    new UpdateBookController(
      stubControllerComponents(),
      mockBookRepository
    )

  private val book1 = new Book(
    id = "book_id_1",
    name = Name("Test Book1"),
    author = Some(Author("Test Author1")),
    publishedDate = Some(PublishedDate(LocalDate.of(2019, 4, 1))),
    description = Some(Description("Test Description1"))
  )

  "index()" should {

    "BDに対象本のデータが存在する場合、編集画面を表示する" in {
      when(mockBookRepository.findById("book_id_1")).thenReturn(Future.successful(Some(book1)))
      val result           = controller.index("book_id_1").apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(status(result) === OK)
      assert(bodyText contains ("Test Book1"))
    }

    "BDに対象本のデータが存在しない場合、NotFound(404)を表示する" in {
      when(mockBookRepository.findById("book_id_1")).thenReturn(Future.successful(None))
      val result           = controller.index("book_id_1").apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(status(result) === NOT_FOUND)
      assert(bodyText contains ("idが見つかりませんでした。"))
    }

    "bookRepository.findById()で例外が発生した場合、InternalServerError(500)を表示する" in {
      when(mockBookRepository.findById("book_id_1")).thenReturn(Future.failed(new Exception("Something happened")))
      val result           = controller.index("book_id_1").apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(status(result) === INTERNAL_SERVER_ERROR)
      assert(bodyText contains ("Something happened"))
    }

  }

  "update()" should {

    "本の情報を更新できる" in {

      val updatingBook = new Book(
        id = "book_id_1",
        name = Name("Test Book1 Update"),
        author = Some(Author("Test Author1  Update")),
        publishedDate = Some(PublishedDate(LocalDate.of(2019, 5, 1))),
        description = Some(Description("Test Description1 Update"))
      )

      val request = FakeRequest(POST, "/books/update").withFormUrlEncodedBody(
        "id"            -> "book_id_1",
        "name"          -> "Test Book1 Update",
        "author"        -> "Test Author1  Update",
        "publishedDate" -> "2019-05-01",
        "description"   -> "Test Description1 Update"
      )

      when(mockBookRepository.update(updatingBook)).thenReturn(Future.successful(()))

      val result = controller.update().apply(request.withCSRFToken)
      assert(status(result) === SEE_OTHER)
    }

    "BookRepository.updateで例外が発生した場合、InternalServerError(500)を返す" in {
      val updatingBook = new Book(
        id = "book_id_1",
        name = Name("Test Book1 Update"),
        author = Some(Author("Test Author1  Update")),
        publishedDate = Some(PublishedDate(LocalDate.of(2019, 5, 1))),
        description = Some(Description("Test Description1 Update"))
      )

      val request = FakeRequest(POST, "/books/update").withFormUrlEncodedBody(
        "id"            -> "book_id_1",
        "name"          -> "Test Book1 Update",
        "author"        -> "Test Author1  Update",
        "publishedDate" -> "2019-05-01",
        "description"   -> "Test Description1 Update"
      )

      when(mockBookRepository.update(updatingBook)).thenReturn(Future.failed(new Exception("Something happened")))

      val result = controller.update().apply(request.withCSRFToken)
      assert(status(result) === INTERNAL_SERVER_ERROR)
    }

  }
}
