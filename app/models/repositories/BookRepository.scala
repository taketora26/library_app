package models.repositories

import models.Book
import scala.concurrent.Future

trait BookRepository {

  def findAll(): Future[Seq[Book]]

  def add(book: Book): Future[Unit]

  def findByName(name: String): Future[Seq[Book]]

  def update(book: Book): Future[Unit]

  def findById(bookId: String): Future[Option[Book]]

  def delete(bookId: String): Future[Unit]

  def searchName(name: String): Future[Seq[Book]]

}
