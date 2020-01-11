package infra.rdb

import infra.rdb.records.BookRecord
import javax.inject.{Inject, Singleton}
import models._
import models.repositories.BookRepository
import scalikejdbc.{DB, _}

import scala.concurrent.Future

@Singleton
class BookRepositoryOnJDBC @Inject() (implicit ec: ExecutionContextOnJDBC) extends BookRepository {

  val b: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[BookRecord], BookRecord] =
    BookRecord.syntax("b")

  def findAll(): Future[Seq[Book]] = Future {
    DB readOnly { implicit session =>
      sql"select * from books as b LIMIT 200 "
        .map(BookRecord(b.resultName))
        .list()
        .apply()
        .map(toModel)
    }
  }

  def add(book: Book): Future[Unit] = Future {
    val record = BookRecord(book)
    DB localTx { implicit session =>
      sql"""insert into books (id, name, author, published_date, description)
           |values (
           |${record.id},
           |${record.name},
           |${record.author},
           |${record.publishedDate},
           |${record.description}
           |)
        """.stripMargin
        .update()
        .apply()
    }
  }

  def findByName(name: String): Future[Seq[Book]] = Future {
    DB readOnly { implicit session =>
      sql"select b.* from books as b where name = $name".map(BookRecord(b.resultName)).list().apply().map(toModel)
    }
  }

  def update(book: Book): Future[Unit] = Future {
    val record = BookRecord(book)
    DB localTx { implicit session =>
      sql"""update books
           | set
           | name = ${record.name},
           | author = ${record.author},
           | published_date = ${record.publishedDate},
           | description = ${record.description}
           | where id = ${record.id}
        """.stripMargin
        .update()
        .apply()
    }
  }

  def findById(bookId: String): Future[Option[Book]] = Future {
    DB readOnly { implicit session =>
      sql"select b.* from books as b where id = $bookId".map(BookRecord(b.resultName)).headOption().apply().map(toModel)
    }
  }

  def delete(bookId: String): Future[Unit] = Future {
    DB localTx { implicit session =>
      sql"delete from books where id = $bookId".update().apply()
    }
  }

  def searchName(name: String): Future[Seq[Book]] = Future {
    DB readOnly { implicit session =>
      val searchName = s"%$name%"
      sql"select b.* from books as b where name like $searchName"
        .map(BookRecord(b.resultName))
        .list()
        .apply()
        .map(toModel)
    }
  }

  private val toModel: BookRecord => Book = { bookRecord =>
    new Book(
      bookRecord.id,
      Name(bookRecord.name),
      bookRecord.author.map(Author),
      bookRecord.publishedDate.map(PublishedDate(_)),
      bookRecord.description.map(Description)
    )
  }

}
