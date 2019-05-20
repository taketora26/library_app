package infra.rdb

import akka.actor.ActorSystem
import javax.inject.{Inject, Singleton}
import play.api.libs.concurrent.CustomExecutionContext

@Singleton
class ExecutionContextOnJDBC @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "jdbc-dispatcher")
