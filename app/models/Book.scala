package models

import java.time.LocalDate
import java.util.UUID

import models.exception.DuplicateBookNameException

import scala.util.{Failure, Success, Try}

case class Book(id: String,
                name: Name,
                author: Option[Author],
                publishedDate: Option[PublishedDate],
                description: Option[Description])

object Book {

  def apply(nameString: String,
            authorString: Option[String],
            publishedDateLocal: Option[LocalDate],
            descriptionString: Option[String]): Book = {
    val uuid = UUID.randomUUID.toString
    new Book(
      id = uuid,
      name = Name(nameString),
      author = if (authorString.isEmpty) None else authorString.map(Author),
      publishedDate = if (publishedDateLocal.isEmpty) None else publishedDateLocal.map(p => PublishedDate(p)),
      description = if (descriptionString.isEmpty) None else descriptionString.map(Description)
    )
  }

  def apply(id: String,
            nameString: String,
            authorString: Option[String],
            publishedDateString: Option[LocalDate],
            descriptionString: Option[String]): Book = {
    new Book(
      id = id,
      name = Name(nameString),
      author = if (authorString.isEmpty) None else authorString.map(Author),
      publishedDate = if (publishedDateString.isEmpty) None else publishedDateString.map(p => PublishedDate(p)),
      description = if (descriptionString.isEmpty) None else descriptionString.map(Description)
    )
  }

  def canRegister(books: Seq[Book]): Try[Unit] = {
    books match {
      case Nil => Success(())
      case _   => Failure(new DuplicateBookNameException("すでに同一の名前の本が登録されています。"))
    }
  }
}
