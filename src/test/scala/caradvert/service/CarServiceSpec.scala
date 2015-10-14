package caradvert.service

import caradvert.db._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import caradvert.http.CarAdvertWorkerActor._
import org.scalatest.{FlatSpec,Matchers}
import scala.collection.JavaConversions._
import java.text.SimpleDateFormat
import org.mockito.Matchers.anyInt

/**
 * there should be more test cases, especially for the exceptional behavior
 * **/
class CarServiceSpec extends FlatSpec with Matchers  with MockitoSugar {
    
  "an empty list"  should "be returned" in {
      val mDao = mock[DynamoCarDao]
      val service = new CarService (mDao);
    
      when(mDao.findAll()).thenReturn(java.util.Collections.emptyIterator[Car]())
      assert (service.findAllCars() === List.empty)     
    }
    
    "a list with two elements" should "be returned" in {
      val mDao = mock[DynamoCarDao]
      val service = new CarService (mDao);
      
      val car: Car = mock[Car]
      val toReturn: java.util.Iterator[Car] = Iterator[Car](car, car)
      println(toReturn)

      when (mDao.findAll().asInstanceOf[java.util.Iterator[Car]]).thenReturn(toReturn)
      assert (service.findAllCars().length == 2)
    }  
    
    val df = new SimpleDateFormat("yyyy-MM-dd")
    
    "a CarDataValidationException " should "not be thrown (insert)" in {
      val mDao = mock[DynamoCarDao]
      val service = new CarService (mDao);
      val carDO:CarDO = CarDO(232,"testcar","gasoline",322, false, Some(1232), Some(df.parse("2013-02-03")))
      val car: Car = mock[Car]
      when (mDao.put(car)).thenReturn(_:Unit)     
      service.insertCar(carDO)
    }
    
    "a CarDataValidationException " should "be thrown (insert)" in {
      val mDao = mock[DynamoCarDao]
      val service = new CarService (mDao);
      val carDO:CarDO = CarDO(232,"testcar","gasoline",322, false, Some(1232), None)
      intercept[CarDataValidationException] {
        service.insertCar(carDO)
      }
    }
    
    "a CarDataValidationException " should "not be thrown (update)" in {
      val mDao = mock[DynamoCarDao]
      val service = new CarService (mDao);
      val carDO:CarDO = CarDO(232,"testcar","gasoline",322, false, Some(1232), Some(df.parse("2013-02-03")))
      val car: Car = mock[Car]
      when(mDao.load(anyInt())).thenReturn(car)
      when (mDao.put(car)).thenReturn(_:Unit)     
      service.updateCar(carDO)
    }
  
    "a CarNotFoundException " should "be thrown (delete)" in {
      val mDao = mock[DynamoCarDao]
      val service = new CarService (mDao);
     
       when(mDao.load(anyInt())).thenReturn(null)  
      intercept[CarNotFoundException] {
        service.deleteCar(232)
      }
    }
  
}