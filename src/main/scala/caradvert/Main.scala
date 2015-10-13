package caradvert;

import caradvert.db.DynamoCarDao
import java.text.SimpleDateFormat


object demo extends App {
  val dao = new DynamoCarDao();
//  dao.destroy()
  dao.setup()    
}