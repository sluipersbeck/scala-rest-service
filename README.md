# scala-rest-service
* Built with Scala 2.11.7

## Used libraries
* Spray 1.3.3
* seratch/awscala 0.5, AWS Java SDK 1.10
* Akka 2.3.9
* Json4s
* Scalatest, Mockito

## Installation
1. Start DynamoDB (local)
```
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```
2. git clone https://github.com/sluipersbeck/scala-rest-service.git
3. cd scala-rest-service && sbt run


## Testing
```
sbt test
```

### CLI Commands for testing 
```
curl -H "Content-Type: application/json" -X POST -d '{"id":1,"title":"BMW X3", "fuel":"gasoline","newCar":true,"price":15000}' http://localhost:8080/api/v1/caradvert/add

curl -H "Content-Type: application/json" -X POST -d '{"id":1,"title":"BMW X3", "fuel":"gasoline","newCar":false,"price":10000, "mileage":12343,"firstRegistration":"2013-10-23" }' http://localhost:8080/api/v1/caradvert/modify

curl -X GET http://localhost:8080/api/v1/caradvert/get/1

curl -X GET http://localhost:8080/api/v1/caradvert/getAll

curl -X GET http://localhost:8080/api/v1/caradvert/delete/1
```
