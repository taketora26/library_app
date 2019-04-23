package infra.rdb

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

}
