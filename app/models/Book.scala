package models

import java.time.LocalDate
import java.util.UUID
import scalikejdbc._

case class Book(id: String,
                name: Name,
                author: Option[Author],
                publishedDate: Option[PublishedDate],
                description: Option[Description])

object Book extends SQLSyntaxSupport[Book] {

  def apply(nameString: String,
            authorString: Option[String],
            publishedDateString: Option[LocalDate],
            descriptionString: Option[String]): Book = {
    val uuid = UUID.randomUUID.toString
    new Book(
      id = uuid,
      name = Name(nameString),
      author = if (authorString.isEmpty) None else authorString.map(Author),
      publishedDate = if (publishedDateString.isEmpty) None else publishedDateString.map(p => PublishedDate(p)),
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

  override def tableName: String = "books"

  def apply(rn: ResultName[Book])(rs: WrappedResultSet) = new Book(
    rs.string("id"),
    Name(rs.string("name")),
    rs.stringOpt("author").map(Author),
    rs.localDateOpt("published_date").map(PublishedDate(_)),
    rs.stringOpt("description").map(Description)
  )

}
