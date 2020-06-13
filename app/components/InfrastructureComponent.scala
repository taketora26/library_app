package components

import infra.rdb.BookRepositoryOnJDBC
import models.repositories.BookRepository

trait InfrastructureComponent {

  import com.softwaremill.macwire._

  lazy val bookRepository: BookRepository = wire[BookRepositoryOnJDBC]

}
