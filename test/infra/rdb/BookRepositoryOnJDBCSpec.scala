package infra.rdb
import org.scalatest.fixture.FlatSpec
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc.{SQL, _}

class BookRepositoryOnJDBCSpec extends FlatSpec with AutoRollback {

  val repository = new BookRepositoryOnJDBC

  config.DBsWithEnv("test").setupAll

  override def fixture(implicit session: FixtureParam): Unit = {
    SQL(
      "INSERT INTO `books` (`id`, `name`, `author`, `published_date`, `description`) VALUES ('test1_id', 'test1_name', 'test1_name', '2016-02-04', 'test1_description')"
    ).update.apply()
  }

}
