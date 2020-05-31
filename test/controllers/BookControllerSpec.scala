package controllers

import java.time.LocalDate

import infra.rdb.ContextOnJDBC
import models.Book
import models.repositories.BookRepository
import org.mockito.Mockito._
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, stubControllerComponents, _}

import scala.util.{Failure, Success}

class BookControllerSpec extends PlaySpec with Results {

  private val mockBookRepository   = mock(classOf[BookRepository])
  private implicit val mockContext = mock(classOf[ContextOnJDBC])

  private val controller =
    new BookController(
      stubControllerComponents(),
      mockBookRepository,
      mockContext
    )

  private val books = List(
    Book(
      nameString = "name_1",
      authorString = Some("author_1"),
      publishedDateLocal = Some(LocalDate.of(2019, 4, 1)),
      descriptionString = Some("description_1")
    ),
    Book(
      nameString = "name_2",
      authorString = Some("author_2"),
      publishedDateLocal = Some(LocalDate.of(2018, 3, 1)),
      descriptionString = Some("description_2")
    ),
    Book(
      nameString = "name_3",
      authorString = Some("author_3"),
      publishedDateLocal = Some(LocalDate.of(2017, 2, 1)),
      descriptionString = Some("description_3")
    )
  )

  private val book4 = Book(
    nameString = "name_4",
    authorString = Some("author_4"),
    publishedDateLocal = Some(LocalDate.of(2019, 4, 1)),
    descriptionString = Some("description_4")
  )

  "index()" should {

    "登録した本の一覧画面を表示する" in {
      when(mockBookRepository.findAll()).thenReturn(Success(books))
      val result           = controller.index().apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(status(result) === OK)
      assert(bodyText contains "name_3")
    }

    "登録した本の一覧画面に本の名前「name_3」が含まれている" in {
      when(mockBookRepository.findAll()).thenReturn(Success(books))
      val result           = controller.index().apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(bodyText contains "name_3")
    }

    "BookRepository.findAll()で例外が発生した場合に、Internal Server Error(500)を返す" in {
      when(mockBookRepository.findAll()).thenReturn(Failure(new Exception("Something happened")))
      val result           = controller.index().apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(status(result) === INTERNAL_SERVER_ERROR)
    }

    "BookRepository.findAll()で例外が発生した場合に、例外のメッセージが表示される" in {
      when(mockBookRepository.findAll()).thenReturn(Failure(new Exception("Something happened")))
      val result           = controller.index().apply(FakeRequest().withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(bodyText contains "Something happened")
    }
  }

  "search()" should {

    "検索したタイトルに部分一致する本の一覧を表示する" in {
      when(mockBookRepository.searchName("name_4")).thenReturn(Success(Seq(book4)))

      val request = FakeRequest(POST, "/books/search").withFormUrlEncodedBody(
        "name" -> "name_4"
      )

      val result           = controller.search().apply(request.withCSRFToken)
      val bodyText: String = contentAsString(result)

      assert(status(result) === OK)
      assert(bodyText.contains("name_4"))
    }

    "BookRepository.searchNameで例外が発生した場合に、InternalServerError(500)を表示する" in {
      when(mockBookRepository.searchName("name_4")).thenReturn(Failure(new Exception("Something happened")))

      val request = FakeRequest(POST, "/books/search").withFormUrlEncodedBody(
        "name" -> "name_4"
      )
      val result           = controller.search().apply(request.withCSRFToken)
      val bodyText: String = contentAsString(result)
      assert(status(result) === INTERNAL_SERVER_ERROR)
      assert(bodyText.contains("Something happened"))
    }
  }

}
