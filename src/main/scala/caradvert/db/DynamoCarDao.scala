package caradvert.db

import awscala._
import awscala.dynamodbv2._
import com.amazonaws.services.dynamodbv2.datamodeling.{DynamoDBMapper,DynamoDBMapperConfig,DynamoDBScanExpression,DynamoDBSaveExpression}

trait DynamoCar {
  var carAdvertTableName: String = "CarAdverts"
  
  implicit val dynamoDB = DynamoDB.local() //should be set somewhere else (could be based on property)
  implicit val mapper = new DynamoDBMapper(dynamoDB)
  
  def setup() 
  def findAll(): java.util.Iterator[Car]
  def put(car: Car)
  def delete(car: Car)
  def load (id: Int): Car 
}

/**
 * simple dynamoDB Car entity DAO
 * does not do any error handling (the task is done by the "business layer")
 */
class DynamoCarDao extends DynamoCar{
  
  def setup() = {
    val tableExists: Boolean = dynamoDB.listTables().getTableNames.contains(carAdvertTableName)
    if (!tableExists) {
      val tableMeta: TableMeta = dynamoDB.createTable(
        name = carAdvertTableName, 
        hashPK = "id" -> AttributeType.Number
      )
    }
  }
  
  /** used just for testing*/
  def destroy() = {
    val tableExists: Boolean = dynamoDB.listTables().getTableNames.contains(carAdvertTableName)
    if (tableExists) {
      dynamoDB.table(carAdvertTableName).get.destroy()
    }
  }
  
  def put(car: Car) = {
    mapper.save(car);
  }
  
  def delete(car: Car) = {
    mapper.delete(car) 
  }
  
  def findAll() = {   
    mapper.scan(classOf[Car], new DynamoDBScanExpression()).iterator()
  }
  
  def load (id: Int): Car = {
    mapper.load(classOf[Car], id)
  }
}

