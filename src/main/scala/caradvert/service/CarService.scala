package caradvert.service

import akka.actor.Actor
import caradvert.db._;
import java.text.SimpleDateFormat
import java.util.Date

object CarService {
  case class FindCar(id: Int)
}

class CarService extends Actor {
  import caradvert.service.CarService.FindCar
  
  def receive: Receive = {
    case FindCar (id) => findCarById(id) 
  }
  
  def findCarById (id: Int) {
    val c = (new DynamoCarDao()).load(id)
    if (c != null) {
      mapToDO (c);
    }
    //TODO error handling
  }
  
  val df = new SimpleDateFormat ("yyyy-MM-dd")
  
  def mapToDO (dbCar:Car) : CarDO = {
    var registration: Option[Date] = null
    if (dbCar.registrationDate != null) {
       registration = Option (df.parse(dbCar.registrationDate))
    }
    CarDO(dbCar.id, dbCar.title, dbCar.fuel, dbCar.price, dbCar.newCar,Some(dbCar.mileage), registration)
  }
  
  def findAllCars(): List[CarDO] = {
    val allCars: List[CarDO] = List.empty
    val dbCars = new DynamoCarDao().findAll()
    while (dbCars.hasNext()) {
      allCars:+mapToDO(dbCars.next())
    }
    allCars
  }
}