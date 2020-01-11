package controllers.forms

import java.time.LocalDate

import play.api.data.Form
import play.api.data.Forms.{localDate, mapping, nonEmptyText, optional, text}

case class BookRegister(
    name: String,
    author: Option[String],
    publishedDate: Option[LocalDate],
    description: Option[String]
)

object BookRegister {

  val form: Form[BookRegister] = Form(
    mapping(
      "name"          -> nonEmptyText,
      "author"        -> optional(text),
      "publishedDate" -> optional(localDate("yyyy-MM-dd")),
      "description"   -> optional(text)
    )(BookRegister.apply)(BookRegister.unapply)
  )

}
