import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Provides}
import infra.rdb.{BookRepositoryOnJDBC, ExecutionContextOnJDBC}
import models.repositories.BookRepository

class GuiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BookRepository]).to(classOf[BookRepositoryOnJDBC])
  }
  private val ecOnJDBC = new ExecutionContextOnJDBC(ActorSystem())

  @Provides
  def provideExecutionContextOnJDBC: ExecutionContextOnJDBC = ecOnJDBC

}
