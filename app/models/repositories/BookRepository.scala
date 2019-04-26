package models.repositories

import models.Book
import scala.util.Try

trait BookRepository {

  def findAll(): Try[Seq[Book]]

  def add(book: Book): Try[Unit]

  def findByName(name: String): Try[Seq[Book]]

  def update(book: Book): Try[Unit]

  def findById(bookId: String): Try[Option[Book]]

  def delete(bookId: String): Try[Unit]

  def searchName(name: String): Try[Seq[Book]]

}
