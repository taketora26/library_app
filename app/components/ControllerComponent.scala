package components

import controllers.{BookController, Default, DeleteBookController, RegisterBookController, UpdateBookController}
import play.api.mvc.ControllerComponents

trait ControllerComponent extends InfrastructureComponent with ContextComponent {

  def controllerComponents: ControllerComponents

  import com.softwaremill.macwire._

  lazy val bookController: BookController                 = wire[BookController]
  lazy val deleteBookController: DeleteBookController     = wire[DeleteBookController]
  lazy val registerBookController: RegisterBookController = wire[RegisterBookController]
  lazy val updateBookController: UpdateBookController     = wire[UpdateBookController]
  lazy val default: Default                               = wire[Default]

}
