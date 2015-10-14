package caradvert.http

import akka.actor.{ Actor, ActorSystem, Props, ActorLogging }
import akka.io.IO
import akka.routing._
import org.json4s._
import org.json4s.native.JsonMethods._
import caradvert.service.CarDO
import caradvert.db.DynamoCarDao
import caradvert.service._


object CarAdvertWorkerActor {
  case class Status(status: Int, message:String)
  case class GetCarResponse(car: CarDO)
  case class GetAllCarsResponse(carAdverts: List[CarDO])
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

  val carService = new CarService(new DynamoCarDao()) 
  def receive = {
    case Create(car) => {
      try {
        carService.insertCar(car)
      } catch { //error messages should be read from property files
        case cafe: CarAlreadyFoundException => sender ! Status(1, "Car advert already exists")
        case cdve: CarDataValidationException => sender! Status(1, "Input data invalid")
        case _ :Exception => sender ! Status(1, "Internal Error")
      }
      sender ! Status(0, "Ok")
    //  carService ! InsertCar(car)
    }
    case Modify(car) => {
      try {
        carService.updateCar(car)
      } catch { //error messages should be read from property files
        case cafe: CarNotFoundException => sender ! Status(1, "Car advert does not exists")
        case cdve: CarDataValidationException => sender! Status(1, "Input data invalid")
        case _ :Exception => sender ! Status(1, "Internal Error")
      }
      sender ! Status(0, "Ok")
//      carService ! UpdateCar(car)
    }
    case GetAllCars() => {
      val cars = carService.findAllCars()
      sender ! GetAllCarsResponse(cars)
     // carService ! FindAllCars()
    }
    case GetCar(id:Int) => {
     val car = carService.findCarById(id)
     sender ! GetCarResponse(car)
     // carService ! FindCar(id)
    }
    case DeleteCar(id:Int) => {
      try {
        carService.deleteCar(id:Int)
      } catch {
        case cnfe: CarNotFoundException=> sender ! Status(1, "Car advert does not exists")
        case e: Exception => sender ! Status(1, e.getMessage())
      }
      sender ! Status(0, "Ok")
    }
  }
}