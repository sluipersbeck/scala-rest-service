package caradvert.http

import akka.actor.{ Actor, ActorSystem, Props, ActorLogging }
import akka.io.IO
import akka.routing._
import org.json4s._
import org.json4s.native.JsonMethods._
import caradvert.service.CarDO

object CarAdvertWorkerActor {
  case class Ok(status: Int)
  case class CreateJSONResponse(carAdverts: List[CarDO])
  case class Create(car: CarDO)
  case class Modify(car: CarDO)
}

class CarAdvertWorkerActor extends Actor with ActorLogging {
  import CarAdvertWorkerActor._
  import caradvert.service.CarService
  import caradvert.service.CarService._

  val carService = context.actorOf(Props[CarService])
  def receive = {
    case Create(car) => {
      carService ! InsertCar(car)
    }
    case Modify(car) => {
      carService ! UpdateCar(car)
    }
    
  }
}