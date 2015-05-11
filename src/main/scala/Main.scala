import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext

/**
 * User: Shubert Alexandr
 * 5/4/15
 */
object Main extends App{
  val server = new Server(8080)

  val app = new WebAppContext
  app.setContextPath("/")
  app.setResourceBase("webapp")
  app.setServer(server)

  server.setHandler(app)
  server.start()
  server.join()
}
