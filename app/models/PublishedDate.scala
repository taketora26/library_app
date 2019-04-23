package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class PublishedDate(value: LocalDate)

object PublishedDate {

  val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def apply(dateString: String): Either[Exception, PublishedDate] = {
    try {
      val date = LocalDate.parse(dateString, dateTimeFormatter)
      Right(PublishedDate(date))
    } catch {
      case ex: Exception => Left(ex)
    }
  }

}
