import java.util.concurrent.Executors

import com.google.inject.AbstractModule
import infra.rdb.BookRepositoryOnJDBC
import models.repositories.BookRepository

import scala.concurrent.ExecutionContext

class GuiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BookRepository]).to(classOf[BookRepositoryOnJDBC])
  }

  val rdbExecutionContext: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
}
