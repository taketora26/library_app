package controllers
import infra.rdb.ContextOnJDBC
import models.Book
import models.repositories.BookRepository
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Results
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, stubControllerComponents, _}

import scala.util.{Failure, Success}
class RegisterBookControllerSpec extends PlaySpec with Results {

  private val mockBookRepository   = mock(classOf[BookRepository])
  private implicit val mockContext = mock(classOf[ContextOnJDBC])

  private val controller = new RegisterBookController(
    stubControllerComponents(),
    mockBookRepository,
    mockContext
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
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Success(Nil))
      when(mockBookRepository.add(any[Book])(any[ContextOnJDBC])).thenReturn(Success(()))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> "MyBook1"
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === SEE_OTHER)
    }

    "登録フォームに本のタイトルが入力されていない場合は、BadRequest(400)を返す" in {
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Success(Nil))
      when(mockBookRepository.add(any[Book])(any[ContextOnJDBC])).thenReturn(Success(()))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> ""
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === BAD_REQUEST)
    }

    "BookRepository.findByNameで例外が発生した場合、Internal Server Error(500)を返す" in {
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Failure(new Exception("Something happened")))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> "MyBook1"
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === INTERNAL_SERVER_ERROR)
    }

    "BookRepository.addで例外が発生した場合、Internal Server Error(500)を返す" in {
      when(mockBookRepository.findByName("MyBook1")).thenReturn(Success(Nil))
      when(mockBookRepository.add(any[Book])(any[ContextOnJDBC]))
        .thenReturn(Failure(new Exception("Something happened")))

      val request = FakeRequest(POST, "/books/register").withFormUrlEncodedBody(
        "name" -> "MyBook1"
      )

      val result = controller.register().apply(request.withCSRFToken)
      assert(status(result) === INTERNAL_SERVER_ERROR)
    }
  }
}
