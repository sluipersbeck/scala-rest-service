package caradvert.db

import org.scalatest._
import awscala.dynamodbv2._
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression

/**
 * this test case was used to test behaviour of database during development
 * the caradvert table should be mocked  
 */
class DynamoSpec extends FlatSpec with Matchers with BeforeAndAfter {
  
  val testcar = Car(id = 321, title = "BMW", fuel = "gasoline", price = 300, newCar = false, mileage = 9232, registrationDate = "2013-09-30")
  
  after {
    new DynamoDBMapper(DynamoDB.local()).delete(testcar);
  }
  
  "CarAdvert table" should "be created" in {
    DynamoCarDao.setup();
    DynamoDB.local().listTables().getTableNames.contains(DynamoCarDao.carAdvertTableName) should be (true)
  }
  
  "A car advert " should "be inserted in table " in {
    DynamoCarDao.put(testcar);
    val insertedCar = new DynamoDBMapper(DynamoDB.local()).load(classOf[Car], testcar.id)
    println(insertedCar)
    assert (insertedCar === testcar);
  }
  
  "A car advert " should "be updated" in {
    DynamoCarDao.put(testcar);
    val carToBeUpdated = new DynamoDBMapper(DynamoDB.local()).load(classOf[Car], testcar.id)
    assert (carToBeUpdated === testcar)
    carToBeUpdated.price = 30000 
    DynamoCarDao.put(carToBeUpdated)
    val carAfterUpdate = new DynamoDBMapper(DynamoDB.local()).load(classOf[Car], testcar.id);
    assert (carAfterUpdate !== testcar)
    carAfterUpdate.title should be equals (testcar.title) 
  }
  
  "a car advert " should "be deleted" in {
    DynamoCarDao.put(testcar);
    val carToBeDeleted = new DynamoDBMapper(DynamoDB.local()).load(classOf[Car], testcar.id)
    assert (carToBeDeleted === testcar)
    carToBeDeleted should be equals (testcar)
    DynamoCarDao.delete(testcar.id)
    val allCars = DynamoCarDao.findAll()
    while(allCars.hasNext()) println(allCars.next())
    new DynamoDBMapper(DynamoDB.local()).scan(classOf[Car], new DynamoDBScanExpression()).isEmpty() should  be (true)
  }
  
  "all car adverts " should "be found" in {
    DynamoCarDao.put(testcar);
    val anotherCar = Car(123,"VW Golf 7", fuel="diesel", 12343, true, null, null)
    DynamoCarDao.put(anotherCar)
    val allCars = DynamoCarDao.findAll()
    assert (allCars.next() === testcar) 
    assert (allCars.next() === anotherCar);
    allCars.hasNext() should be (false)
    new DynamoDBMapper(DynamoDB.local()).delete(anotherCar)
  }
}