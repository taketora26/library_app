package controllers
import models.Book
import models.repositories.BookRepository
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Results
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, stubControllerComponents, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
class RegisterBookControllerSpec extends PlaySpec with MockitoSugar with Results {

  private val mockBookRepository = mock[BookRepository]

  private val controller = new RegisterBookController(
    stubControllerComponents(),
    mockBookRepository
  )

  "index()" should {
    "登録画面を表示する" in {
      val result           = controller.index().apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(bodyText.contains("書籍を登録"))
      assert(status(result) === OK)
    }

    "登録画面に「書籍を登録」という文字が表示される" in {
      val result           = controller.index().apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(bodyText.contains("書籍を登録"))
    }
  }

  "register()" should {
    "登録フォームに本のタイトルが入力されていれば、登録後、登録本一覧にリダイレクトされる" in {
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Future.successful(Nil))
      when(mockBookRepository.add(any[Book])).thenReturn(Future.successful(()))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> "MyBook1"
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === SEE_OTHER)
    }

    "登録フォームに本のタイトルが入力されていない場合は、BadRequest(400)を返す" in {
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Future.successful(Nil))
      when(mockBookRepository.add(any[Book])).thenReturn(Future.successful(()))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> ""
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === BAD_REQUEST)
    }

    "BookRepository.findByNameで例外が発生した場合、Internal Server Error(500)を返す" in {
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Future.failed(new Exception("Something happened")))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> "MyBook1"
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === INTERNAL_SERVER_ERROR)
    }

    "BookRepository.addで例外が発生した場合、Internal Server Error(500)を返す" in {
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Future.successful(Nil))
      when(mockBookRepository.add(any[Book])).thenReturn(Future.failed(new Exception("Something happened")))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> "MyBook1"
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === INTERNAL_SERVER_ERROR)
    }
  }
}
