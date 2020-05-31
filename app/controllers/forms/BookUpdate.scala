package controllers.forms

import java.time.LocalDate
import models.Book
import play.api.data.Form
import play.api.data.Forms.{localDate, mapping, nonEmptyText, optional, text}

case class BookUpdate(
    id: String,
    name: String,
    author: Option[String],
    publishedDate: Option[LocalDate],
    description: Option[String]
) {

  def toBookModel: Book = {
    Book(this.id, this.name, this.author, this.publishedDate, this.description)
  }

}

object BookUpdate {

  def apply(book: Book): BookUpdate = {
    new BookUpdate(
      book.id,
      book.name.value,
      book.author.map(_.value),
      book.publishedDate.map(_.value),
      book.description.map(_.value)
    )
  }

  val form: Form[BookUpdate] = Form(
    mapping(
      "id"            -> nonEmptyText,
      "name"          -> nonEmptyText,
      "author"        -> optional(text),
      "publishedDate" -> optional(localDate("yyyy-MM-dd")),
      "description"   -> optional(text)
    )(BookUpdate.apply)(BookUpdate.unapply)
  )

}
