package controllers.forms

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}

case class BookSearch(name: String)

object BookSearch {

  val form: Form[BookSearch] = Form(
    mapping(
      "name" -> nonEmptyText
    )(BookSearch.apply)(BookSearch.unapply)
  )
}
