package infra.rdb
import models.repositories.Context
import scalikejdbc.DBSession

case class ContextOnJDBC(session: DBSession) extends Context
