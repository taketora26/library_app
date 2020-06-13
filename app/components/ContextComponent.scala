package components

import infra.rdb.ContextOnJDBC
import models.repositories.Context
import scalikejdbc.AutoSession

trait ContextComponent {
  implicit val ctx: Context = ContextOnJDBC(AutoSession)
}
