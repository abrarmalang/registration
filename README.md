## What is this?

API for  school registration system

**Technology stack**
* Java 17
* Maven
* Spring boot
* Maven
* Docker (docker-compose)
* JUnit
* MySQL
* Swagger UI (documentation)
* h2 db(testing)


## How to run this?
### First clone and build the application
```bash

$ git clone https://github.com/abrarmalang/registration
$ cd registration
$ docker compose build

```

### Start the application service in background
```bash

$ docker compose up -d

```
### Stop and remove the application services and volumes
```bash

$ docker compose down -v

```


## Endpoints & Payload

### Swagger UI for API documentation
With the application started up Swagger UI can be used **to access interactive API documentation to try out the API calls directly in the browser**. With [API Documentation](http://localhost:8080/swagger-ui/index.html#/).
### API Information
|        Description        |Endpoint                       |Payload                         |
|----------------|-------------------------------|-----------------------------|
|Get Course with id 1 |`GET` http://localhost:8080/api/v1/course/1|            |
|Get All Courses|`GET`http://localhost:8080/api/v1/courses||
|Filter all courses for a specific student (with id 1)         |`GET` http://localhost:8080/api/v1/student/1/courses|
|Filter all courses without any students|`GET` http://localhost:8080/api/v1/filter/courses?filter[unlinked]=true|
|Create course|`POST` http://localhost:8080/api/v1/course |`{"courseName":"Food, Culture & Politics"}`|
|Update Course|`PUT` http://localhost:8080/api/v1/course/6|`{"courseName":"Food, Culture & Weather"}`|
|Delete Course|`DELETE` http://localhost:8080/api/v1/course/6|
|||
|Get Student with id 1 |`GET` http://localhost:8080/api/v1/student/1|            |
|Get All Students|`GET`http://localhost:8080/api/v1/students||
|Filter all students for a specific course (with id 1)         |`GET` http://localhost:8080/api/v1/course/1/students|
|Filter all students without any courses|`GET` http://localhost:8080/api/v1/filter/students?filter[unlinked]=true|
|Create course|`POST` http://localhost:8080/api/v1/course |`{"courseName":"Food, Culture & Politics"}`|
|Update student|`PUT` http://localhost:8080/api/v1/student/6|`{"firstName": "Bob","lastName": "Builder","email": "bob@builder.com","phone": "7078583345","address": "100 Main Street"}`|
|Delete student|`DELETE` http://localhost:8080/api/v1/student/6|


### Postman Collection
Postman collection that can be imported into the postman tool is available in a file called `school.postman_collection.json` and environment variables for local env. in `local.postman_environment.json` within the source repository.  
