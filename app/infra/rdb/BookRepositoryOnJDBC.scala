package infra.rdb

import infra.rdb.records.BookRecord
import models._
import models.repositories.BookRepository
import scalikejdbc.{DB, _}

import scala.util.Try

class BookRepositoryOnJDBC extends BookRepository {

  val b: QuerySQLSyntaxProvider[SQLSyntaxSupport[Book], Book] = Book.syntax("b")

  def findAll(): Try[Seq[Book]] = Try {
    DB readOnly { implicit session =>
      sql"select * from books as b LIMIT 200 "
        .map(Book(b.resultName))
        .list()
        .apply()
    }
  }

  def add(book: Book): Try[Unit] = Try {
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

  def findByName(name: String): Try[Seq[Book]] = Try {
    DB readOnly { implicit session =>
      sql"select b.* from books as b where name = $name".map(Book(b.resultName)).list().apply()
    }
  }

  def update(book: Book): Try[Unit] = Try {
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

  def findById(bookId: String): Try[Option[Book]] = Try {
    DB readOnly { implicit session =>
      sql"select b.* from books as b where id = $bookId".map(Book(b.resultName)).headOption().apply()
    }
  }

  def delete(bookId: String): Try[Unit] = Try {
    DB localTx { implicit session =>
      sql"delete from books where id = $bookId".update().apply()
    }
  }

}
