package caradvert.service

import java.util.Date

//fuel should have been an enum
case class CarDO (id:Int,title:String, fuel:String, price:Int, newCar:Boolean, mileage:Option[Int],firstRegistration: Option[Date]) {
  
}