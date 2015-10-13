package caradvert.service

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}
import caradvert.db.DynamoCarSetup
import org.specs2.mutable.SpecificationLike
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import caradvert.service.CarService._

class CarServiceSpec extends TestKit(ActorSystem()) with SpecificationLike with Mockito /* with CoreActors with Core*/ with ImplicitSender{
  import caradvert.service.CarService
  
 // sequential
  
  "The actor system should" >> {
    val dao = mock[DynamoCarSetup]
    implicit val system = ActorSystem("caradvert-service")
    sys.addShutdownHook(system.shutdown())
    
    val carService = system.actorOf(Props[CarService])
   
    "returns an empty list"  in new Scope {
      println("run empty list test case")
      dao.findAll() returns (java.util.Collections.emptyIterator())
     // CarService.FindCar 
      carService ! FindAllCars
      expectMsg(List.empty)
      success
    }
    
  }
  
  
}