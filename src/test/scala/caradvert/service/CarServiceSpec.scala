package caradvert.service

import caradvert.db._
import org.specs2.mutable.SpecificationLike
//import org.specs2.mock.Mockito
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import caradvert.http.CarAdvertWorkerActor._
import org.scalatest.{FlatSpec,Matchers}
import scala.collection.JavaConversions._

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
    

  
  
}