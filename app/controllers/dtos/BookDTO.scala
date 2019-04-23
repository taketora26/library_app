package controllers.dtos

import java.time.LocalDate
import models.Book

case class BookDTO(id: String,
                   name: String,
                   author: Option[String],
                   publishedDate: Option[LocalDate],
                   description: Option[String])

object BookDTO {

  def apply(book: Book): BookDTO = {
    new BookDTO(
      book.id,
      book.name.value,
      book.author.map(_.value),
      book.publishedDate.map(_.value),
      book.description.map(_.value)
    )
  }
}
