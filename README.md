## task-manager-service
task-manager-service

### How to Run
Please follow the below instructions to run the application:

* Clone this git repository
* Make sure you are using JDK 1.8 (or above) and Maven 3.x
* You can build the project and run the tests by running mvn clean install
* After successful build, you can run the service using the below command:

```
mvn spring-boot:run
```

## Rest Endpoints

### list of tasks

```
http://localhost:8080/api/v1/tasks

Method: GET
Response: HTTP 200
Content: list of json object 
```

### create tasks

```
http://localhost:8080/api/v1/task

Method: POST
Response: HTTP 201
Content: create task and store into the db 
```

## Try Out

### Please use the below swagger link to try the rest api's

```
http://localhost:8080/swagger-ui/index.html
```
