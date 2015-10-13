package caradvert;

import caradvert.db.DynamoCarDao
import java.text.SimpleDateFormat
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import caradvert.http._
import caradvert.service.CarService

/** class that starts everything (main) */
object main extends App {
  val dao = new DynamoCarDao();
//  dao.destroy()
  dao.setup()    
  implicit val system = ActorSystem("caradvert-service")
  sys.addShutdownHook(system.shutdown())
  val service = system.actorOf(Props[RestServiceActor], "caradvert-service")
  //val carService = system.actorOf(Props[CarService], "caradvert-service")

    //If we're on cloud foundry, get's the host/port from the env vars
  lazy val host = Option(System.getenv("CARADVERT_APP_HOST")).getOrElse("localhost")
  lazy val port = Option(System.getenv("CARADVERT_APP_PORT")).getOrElse("8080").toInt

  IO(Http) ! Http.Bind(service, host, port = port)
}