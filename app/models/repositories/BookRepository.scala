package models.repositories

import models.Book
import scala.util.Try

trait BookRepository {

  def findAll()(implicit ctx: Context): Try[Seq[Book]]

  def add(book: Book)(implicit ctx: Context): Try[Unit]

  def findByName(name: String)(implicit ctx: Context): Try[Seq[Book]]

  def update(book: Book)(implicit ctx: Context): Try[Unit]

  def findById(bookId: String)(implicit ctx: Context): Try[Option[Book]]

  def delete(bookId: String)(implicit ctx: Context): Try[Unit]

  def searchName(name: String)(implicit ctx: Context): Try[Seq[Book]]

}
