package infra.rdb
import models.repositories.Context
import scalikejdbc.DBSession

trait RepositoryOnJDBC {

  def withDBSession[A](ctx: Context)(execution: DBSession => A): A =
    ctx match {
      case ContextOnJDBC(session) => execution(session)
      case _                      => throw new IllegalStateException(s"Context:${ctx} は定義していないContextです。")
    }
}
