package caradvert.http

import akka.actor.{ Actor, ActorSystem, Props, ActorLogging }
import akka.io.IO
import akka.routing._
import org.json4s._
import org.json4s.native.JsonMethods._
import caradvert.service.CarDO

object CarAdvertWorkerActor {
  case class Ok(status: Int)
  case class FindCarResponse(car: CarDO)
  case class FindAllCarsResponse(car: List[CarDO])
  case class Create(car: CarDO)
  case class Modify(car: CarDO)
  case class DeleteCar(id: Int)
  case class GetAllCars()
  case class GetCar(id: Int)
}

class CarAdvertWorkerActor extends Actor with ActorLogging {
  import CarAdvertWorkerActor._
  import caradvert.service.CarService
  import caradvert.service.CarService._

  val carService = new CarService() //context.actorOf(Props[CarService])
  def receive = {
    case Create(car) => {
      carService.insertCar(car)
      sender ! Ok(200)
    //  carService ! InsertCar(car)
    }
    case Modify(car) => {
      carService.updateCar(car)
      sender ! Ok(200)
//      carService ! UpdateCar(car)
    }
    case GetAllCars() => {
      val cars = carService.findAllCars()
      println ("all Cars size " +cars.size); 
      for (car <- cars) println ("in worker " +car)
      sender ! FindAllCarsResponse(cars)
     // carService ! FindAllCars()
    }
    case GetCar(id:Int) => {
     val car = carService.findCarById(id)
     sender ! FindCarResponse(car)
     // carService ! FindCar(id)
    }
    case DeleteCar(id:Int) => {
      carService.deleteCar(id:Int)
      sender ! Ok(200)
    }
  }
}