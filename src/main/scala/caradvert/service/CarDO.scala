package caradvert.service

import java.util.Date

case class CarDO (Id:Int,title:String, fuel:String, price:Int, newCar:Boolean, mileage:Option[Int],firstRegistration: Option[Date]) {
  
}