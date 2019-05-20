import java.util.concurrent.Executors

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Provides}
import infra.rdb.{BookRepositoryOnJDBC, ExecutionContextOnJDBC}
import models.repositories.BookRepository

import scala.concurrent.ExecutionContext

class GuiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BookRepository]).to(classOf[BookRepositoryOnJDBC])
  }

  val rdbExecutionContext: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  private val ecOnJDBC = new ExecutionContextOnJDBC(ActorSystem())

  @Provides
  def provideExecutionContextOnJDBC: ExecutionContextOnJDBC = ecOnJDBC

}
