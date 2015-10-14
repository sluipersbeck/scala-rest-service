package caradvert.service

import caradvert.db._
import org.specs2.mutable.SpecificationLike
//import org.specs2.mock.Mockito
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import caradvert.http.CarAdvertWorkerActor._
import org.scalatest.{FlatSpec,Matchers}
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList

class CarServiceSpec extends FlatSpec with Matchers  with MockitoSugar {
    
    val service = new CarService ();
    
   
    "an empty list"  should "be returned" in {
      val dao = mock[DynamoCarDao]
      println("run empty list test case")
      
      when(dao.findAll()).thenReturn(java.util.Collections.emptyIterator[Car]())
      assert (service.findAllCars() === List.empty)     
    }
    
    //does not work
    "a list with two elements" should "be returned" in {
     // val dao = mock[DynamoCar]
      val ml = mock[PaginatedScanList[Car]]
      val stubCars = new java.util.ArrayList[Car]()
      stubCars.add(Car(232,"testcar","gasoline",322, true, null, null))
      stubCars.add(Car(333, "another", "diesel", 323, false, 1021, "2014-03-04"))
//      println("stub car 0 " +stubCars.get(0)) 
      when(ml.iterator()).thenReturn(stubCars.iterator())
      val cars = service.findAllCars()
//       while(it.hasNext()) println ("check iterator" + it.next())
//      for(car <- cars) println (car + "in test")
      assert (cars.length == 2)
    }
    

  
  
}