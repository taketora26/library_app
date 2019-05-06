package infra.rdb

import infra.rdb.records.BookRecord
import models._
import models.repositories.{BookRepository, Context}
import scalikejdbc._

import scala.util.Try

class BookRepositoryOnJDBC extends BookRepository with RepositoryOnJDBC {

  val b: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[BookRecord], BookRecord] =
    BookRecord.syntax("b")

  def findAll()(implicit ctx: Context): Try[Seq[Book]] = Try {
    withDBSession(ctx) { implicit session =>
      sql"select * from books as b LIMIT 200 "
        .map(BookRecord(b.resultName))
        .list()
        .apply()
        .map(toModel)
    }
  }

  def add(book: Book)(implicit ctx: Context): Try[Unit] = Try {
    val record = BookRecord(book)
    withDBSession(ctx) { implicit session =>
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

  def findByName(name: String)(implicit ctx: Context): Try[Seq[Book]] = Try {
    withDBSession(ctx) { implicit session =>
      sql"select b.* from books as b where name = $name".map(BookRecord(b.resultName)).list().apply().map(toModel)
    }
  }

  def update(book: Book)(implicit ctx: Context): Try[Unit] = Try {
    val record = BookRecord(book)
    withDBSession(ctx) { implicit session =>
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

  def findById(bookId: String)(implicit ctx: Context): Try[Option[Book]] = Try {
    withDBSession(ctx) { implicit session =>
      sql"select b.* from books as b where id = $bookId".map(BookRecord(b.resultName)).headOption().apply().map(toModel)
    }
  }

  def delete(bookId: String)(implicit ctx: Context): Try[Unit] = Try {
    withDBSession(ctx) { implicit session =>
      sql"delete from books where id = $bookId".update().apply()
    }
  }

  def searchName(name: String)(implicit ctx: Context): Try[Seq[Book]] = Try {
    withDBSession(ctx) { implicit session =>
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
