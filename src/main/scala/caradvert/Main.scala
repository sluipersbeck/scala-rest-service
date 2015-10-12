package caradvert;

import caradvert.db._
import java.text.SimpleDateFormat


object demo extends App {
  println("### DYNAMOD DB DEMO");
  DynamoCarDao.destroy()
  DynamoCarDao.setup()    
}