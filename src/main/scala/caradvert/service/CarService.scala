package caradvert.service

import akka.actor.Actor
import caradvert.db._
import java.text.SimpleDateFormat
import java.util.Date
import caradvert.service.CarService.InsertCar

case class CarNotFoundException() extends Exception
case class CarDataValidationException() extends Exception
case class CarAlreadyFoundException() extends Exception

/**
 * should have been an asynchronous service 
 */

object CarService {
  case class FindCar(id: Int)
  case class FindAllCars()
  case class UpdateCar(car:CarDO)
  case class InsertCar(car:CarDO)
//  case class Ok(status: Int) //TODO handle errors and correct behavior (maybe not the best approach)
}

class CarService /*extends Actor*/ {
//  import CarService._
  
  val dao = new DynamoCarDao();
//  def receive: Receive = {
//    case FindCar (id) => sender ! findCarById(id) 
//    case InsertCar(car) => sender ! insertCar(car)
//    case UpdateCar(car) => updateCar(car)
//    case FindAllCars() => sender ! findAllCars()
//  }
//  
  def findCarById (id: Int) : CarDO = {
    val c = dao.load(id)
    if (c != null) {
      mapToDO (c)
    } else {
      throw CarNotFoundException()
    }
  }
  
  def findAllCars(): List[CarDO] = {
    var allCars: List[CarDO] = List.empty
    val dbCars = dao.findAll()
    while (dbCars.hasNext()) {
      allCars = allCars:+mapToDO(dbCars.next())
    }
    allCars
  }
  
  def updateCar(car: CarDO) {
    if (dao.load(car.id) == null) {
      throw CarNotFoundException()
    }  
    if (!car.newCar && car.mileage==null && car.mileage==null)
      throw CarDataValidationException()
    dao.put(mapToDB(car));
  }
  
  def insertCar(car: CarDO) {
    if (dao.load(car.id) != null) {
      throw CarAlreadyFoundException()
    }  
    if (!car.newCar && car.mileage==null && car.mileage==null)
      throw CarDataValidationException()
    dao.put(mapToDB(car))
  }
  
  def deleteCar(id:Int) {
    val car = dao.load(id)
    if (car == null) {
      throw CarNotFoundException()
    }  
    dao.delete(car)
  }
  
  val df = new SimpleDateFormat ("yyyy-MM-dd")
  
  def mapToDO(dbCar: Car): CarDO = {
    var registration: Option[Date] = null
    if (dbCar.firstRegistration != null) {
       registration = Option (df.parse(dbCar.firstRegistration))
    }
    var mileage:Option[Int] = null
    if(dbCar.mileage!=null) {
      mileage = Some(dbCar.mileage)
    }
    CarDO(dbCar.id, dbCar.title, dbCar.fuel, dbCar.price, dbCar.newCar,mileage, registration)
  }
  
  def mapToDB(doCar: CarDO): Car = {
    var registration: String = null
    var mileage: Integer = null
    if (!doCar.firstRegistration.isEmpty) {
       registration = df.format(doCar.firstRegistration.get)
    }
    Car(doCar.id, doCar.title, doCar.fuel, doCar.price, doCar.newCar, mileage, registration)
  }
}