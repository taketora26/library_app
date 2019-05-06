import com.google.inject.AbstractModule
import infra.rdb.{BookRepositoryOnJDBC, ContextOnJDBC}
import models.repositories.{BookRepository, Context}
import scalikejdbc.AutoSession

class GuiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BookRepository]).to(classOf[BookRepositoryOnJDBC])
    bind(classOf[Context]).toInstance(ContextOnJDBC(AutoSession))
  }
}
