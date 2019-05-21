package util

import scala.concurrent.Future

object EitherOps {

  implicit class RichEither[E <: Throwable, A](either: Either[E, A]) {
    def toFuture(): Future[A] = {
      either match {
        case Left(e)      => Future.failed(e)
        case Right(value) => Future.successful(value)
      }
    }
  }
}
