# Recipe APP
This is a standalone java application which allows users to manage their favourite recipes.
As a user you allow adding, updating, removing and fetching recipes.

## Requirements
For building and running the application you need:
- JDK 17 
- Maven 3
- Docker
- Docker-compose

##  Technologies
- Spring-boot
- Postgres
- Flyway
- Aysnc APIs
- OpenApi tooling
- Docker


## Running the application locally
There are several ways to run a Spring Boot application on your local machine. You can create the docker image for the app
using the command below
```shell
./mvnw spring-boot:build-image 
```
and then go to the docker folder and run the docker-compose file
```shell
cd docker
docker-compose run  
```
And then you should be able to see the API documentation
http://localhost:7191/api/swagger-ui/index.html?url=/api/json-docs

