package infra.rdb.records

import java.time.LocalDate
import models.Book

case class BookRecord(id: String,
                      name: String,
                      author: Option[String],
                      publishedDate: Option[LocalDate],
                      description: Option[String])

object BookRecord {

  def apply(book: Book): BookRecord = new BookRecord(
    book.id,
    book.name.value,
    book.author.map(_.value),
    book.publishedDate.map(_.value),
    book.description.map(_.value)
  )

}
