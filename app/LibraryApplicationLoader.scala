import _root_.controllers.AssetsComponents
import com.softwaremill.macwire._
import components.ControllerComponent
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import router.Routes
import scalikejdbc.PlayInitializer

class LibraryApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = new LibraryComponents(context).application
}

class LibraryComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with ControllerComponent
    with AssetsComponents
    with play.filters.HttpFiltersComponents {

  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val router: Router = {
    lazy val prefix = "/"
    wire[Routes]
  }

  val dbPlayInitializer: scalikejdbc.PlayInitializer = wire[PlayInitializer]

}
