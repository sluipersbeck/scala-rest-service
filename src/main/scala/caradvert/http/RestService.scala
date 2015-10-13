package caradvert.http


import akka.actor.{ Actor, ActorLogging, ActorSystem, Props }
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{ read, write }
import spray.json.DefaultJsonProtocol
import spray.httpx.Json4sSupport
import spray.routing.HttpService

class RestServiceActor extends Actor with RestRoute {
    def actorRefFactory = context
    def receive = runRoute(route)
}
  
object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = formatsWithDate(new java.text.SimpleDateFormat("yyyy-MM-dd"))
  
  private def formatsWithDate(customDateFormat : java.text.SimpleDateFormat): Formats = {
    new DefaultFormats {
      override val dateFormatter = customDateFormat
    }
  }
}

trait RestRoute extends HttpService  {
   import caradvert.service.CarDO
   import Json4sProtocol._
   import CarAdvertWorkerActor._
   
  implicit val apiVersion = "v1"
  implicit val apiEntity = "caradvert"
   
  //implicit values are need for akka
  implicit def executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(5.seconds)
   
   val worker = actorRefFactory.actorOf(Props[CarAdvertWorkerActor], "caradvert-worker")
   
   val route = {
     pathPrefix("api" / apiVersion / apiEntity) {
       post {
         path("add") {
           entity(as[CarDO]) { car =>  
             doCreate(car)
          }
        } ~ 
        path("modify") {
          entity(as[CarDO]) { car =>  
             doModify(car)
          }
        }
      }
    }
  }
   
  def doCreate[T](car: CarDO) = {
    complete {
    (worker ? Create(car))
      .mapTo[CreateJSONResponse]
      //.map(result => result)
      .recover { case _ => "error" }
    }    
  }
  def doModify[T](car: CarDO) = {
    complete {
    (worker ? Modify(car))
      .mapTo[CreateJSONResponse]
      //.map(result => result)
      .recover { case _ => "error" }
    }
  }
}

