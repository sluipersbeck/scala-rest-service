package caradvert.service

import akka.actor.Actor
import caradvert.db._;
import java.text.SimpleDateFormat
import java.util.Date

case class CarNotFoundException() extends Exception
case class CarDataValidationException() extends Exception
case class CarAlreadyFoundException() extends Exception

object CarService {
  case class FindCar(id: Int)
  case class FindAllCars()
  case class UpdateCar(car:CarDO)
  case class InsertCar(car:CarDO)
}

class CarService extends Actor {
  import caradvert.service.CarService.FindCar
  
  val dao = new DynamoCarDao();
  def receive: Receive = {
    case FindCar (id) => findCarById(id) 
  }
  
  def findCarById (id: Int) = {
    val c = dao.load(id)
    if (c != null) {
      mapToDO (c)
    } else {
      throw CarNotFoundException()
    }
  }
  
  def findAllCars(): List[CarDO] = {
    val allCars: List[CarDO] = List.empty
    val dbCars = dao.findAll()
    while (dbCars.hasNext()) {
      allCars:+mapToDO(dbCars.next())
    }
    allCars
  }
  
  def updateCar(car: CarDO) {
    if (dao.load(car.Id) == null) {
      throw CarNotFoundException()
    }  
    if (!car.newCar && car.mileage==null && car.mileage==null)
      throw CarDataValidationException()
    dao.put(mapToDB(car));
  }
  
  def insertCar(car: CarDO) {
    if (dao.load(car.Id) != null) {
      throw CarAlreadyFoundException()
    }  
    if (!car.newCar && car.mileage==null && car.mileage==null)
      throw CarDataValidationException()
    dao.put(mapToDB(car));
  }
  
  
  val df = new SimpleDateFormat ("yyyy-MM-dd")
  
  def mapToDO(dbCar: Car): CarDO = {
    var registration: Option[Date] = null
    if (dbCar.firstRegistration != null) {
       registration = Option (df.parse(dbCar.firstRegistration))
    }
    CarDO(dbCar.id, dbCar.title, dbCar.fuel, dbCar.price, dbCar.newCar,Some(dbCar.mileage), registration)
  }
  
  def mapToDB(doCar: CarDO): Car = {
    var registration: String = null
    var mileage: Integer = null
    if (doCar.firstRegistration != null) {
       registration = df.format(doCar.firstRegistration)
    }
    Car(doCar.Id, doCar.title, doCar.fuel, doCar.price, doCar.newCar, mileage, registration)
  }
}