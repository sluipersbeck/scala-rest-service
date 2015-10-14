package caradvert.http

import akka.actor.{ Actor, ActorSystem, Props, ActorLogging }
import akka.io.IO
import akka.routing._
import org.json4s._
import org.json4s.native.JsonMethods._
import caradvert.service.CarDO
import caradvert.db.DynamoCarDao

object CarAdvertWorkerActor {
  case class Ok(status: Int)
  case class GetCarResponse(car: CarDO)
  case class GetAllCarsResponse(carAdverts: List[CarDO])
  case class Create(car: CarDO)
  case class Modify(car: CarDO)
  case class DeleteCar(id: Int)
  case class GetAllCars()
  case class GetCar(id: Int)
  case class Error(status:Int,message: String)
}

class CarAdvertWorkerActor extends Actor with ActorLogging {
  import CarAdvertWorkerActor._
  import caradvert.service.CarService
  import caradvert.service.CarService._

  val carService = new CarService(new DynamoCarDao()) //context.actorOf(Props[CarService])
  def receive = {
    case Create(car) => {
      try {
        carService.insertCar(car)
      } catch {
        case e: Exception => sender ! Ok (1)
      }
      sender ! Ok(0)
    //  carService ! InsertCar(car)
    }
    case Modify(car) => {
      carService.updateCar(car)
      sender ! Ok(0)
//      carService ! UpdateCar(car)
    }
    case GetAllCars() => {
      val cars = carService.findAllCars()
      println ("all Cars size " +cars.size); 
      for (car <- cars) println ("in worker " +car)
      sender ! GetAllCarsResponse(cars)
     // carService ! FindAllCars()
    }
    case GetCar(id:Int) => {
     val car = carService.findCarById(id)
     sender ! GetCarResponse(car)
     // carService ! FindCar(id)
    }
    case DeleteCar(id:Int) => {
      carService.deleteCar(id:Int)
      sender ! Ok(0)
    }
  }
}