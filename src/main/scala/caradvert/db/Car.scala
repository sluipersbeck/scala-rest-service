package caradvert.db

import annotation.meta.beanGetter
import beans.BeanProperty
import com.amazonaws.services.dynamodbv2.datamodeling._


@DynamoDBTable(tableName="CarAdverts") //should have some validation
case class Car ( @(DynamoDBHashKey @beanGetter) 
  @BeanProperty var id:Integer,    // must be var otherwise the mapper cannot instantiate them
  @BeanProperty var title: String,
  @BeanProperty var fuel: String,
  @BeanProperty var price: Integer,
  @BeanProperty var newCar: Boolean,
  @BeanProperty var mileage: Integer,
  @BeanProperty var firstRegistration: String //should be of type Date, but I do not have enough time for solving the time zone issue 
  ) {
  def this() = this(null,null,null,null,true,null,null) //needed for the beans
 
 
  def this (id:Integer, title: String, fuel: String, price: Int, newCar: Boolean, mileage:Integer=null, registrationDate:String=null) {
    this(null,null,null,null,true,null,null)
    this.id = id
    this.title = title
    this.fuel = fuel
    this.price = price
    this.newCar = newCar
    this.mileage = mileage
    this.firstRegistration = registrationDate
    
  }
}