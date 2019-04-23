package models.repositories

import models.Book
import scala.util.Try

trait BookRepository {

  def findAll(): Try[Seq[Book]]

  def add(book: Book): Try[Unit]

  def findByName(name: String): Try[Seq[Book]]

}
