package caradvert.db

import awscala._
import awscala.dynamodbv2._
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression

/**
 * simple dynamoDB Car entity DAO
 * does not do any error handling (that is the task of the "business layer")
 */

object DynamoCarDao {
  var carAdvertTableName: String = "CarAdverts"
  
  implicit val dynamoDB = DynamoDB.local()
  implicit val mapper = new DynamoDBMapper(dynamoDB);
  
  
  def setup() = {
    val tableExists: Boolean = dynamoDB.listTables().getTableNames.contains(carAdvertTableName)
    if (!tableExists) {
      val tableMeta: TableMeta = dynamoDB.createTable(
        name = carAdvertTableName, 
        hashPK = "id" -> AttributeType.Number
      )
    }
  }
  
  def destroy() = {
    val tableExists: Boolean = dynamoDB.listTables().getTableNames.contains(carAdvertTableName)
    if (tableExists) {
      dynamoDB.table(carAdvertTableName).get.destroy()
    }
  }
  
  def put(car: Car): Unit = {
    mapper.save(car);
  }
  
  def delete(id:Int): Unit = {
    mapper.delete(load(id)) //TODO move load and add error handling to service layer
  }
  
  def findAll() = {   
    mapper.scan(classOf[Car], new DynamoDBScanExpression()).iterator()
  }
  
  def load (id: Int): Car = {
    mapper.load(classOf[Car], id)
  }
}

