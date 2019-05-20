package infra.rdb

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext

class ExecutionContextOnJDBC @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "jdbc-dispatcher")
