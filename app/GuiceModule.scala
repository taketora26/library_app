import com.google.inject.AbstractModule
import infra.rdb.BookRepositoryOnJDBC
import models.repositories.BookRepository

class GuiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BookRepository]).to(classOf[BookRepositoryOnJDBC])
  }
}
