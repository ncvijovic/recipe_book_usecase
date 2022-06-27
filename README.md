# Recipe book use case

This application provides managing of users recipes.

## Dependencies
- Java 17
- Spring: Boot, Web, Data JPA, Actuator
- Hibernate
- HikariCP
- PostgreSQL
- Flyway
- Lombok
- OpenAPI 3
- Swagger UI
- JUnit 5
- Mockito

## How to start
### Production start
The production environment is not implemented but in case there is, the application is configured to be started with the `prod` profile by simply using:
```
SPRING_PROFILE=prod docker-compose up -d --build
```
###Local start
To start this application locally first you must build it. Make sure to have Java 17. After cloning the project locally, execute this first:
```
./gradlew clean build
```
This will build the application along with running all the testing.
After that execute the following command which will dockerize the application and start up the required dependency:
```
SPRING_PROFILE=local docker-compose up -d --build
```

## Application documentation and testing
The application exposes its documentation via Swagger. To access it, start the application as described above and go to the Swagger endpoint: http://localhost:8080/swagger-ui/index.html

Please note that the application is protected by BasicAuth so you will need to enter the credentials by clicking on the `Authorize` button before trying to use the endpoints. The credentials are:
```
username: user
password: password
```

## Development guidelines
- Security is handled with an in-memory database and basic auth with unencrypted password for the use case. This would be replaced with a proper authentication and role based authorization configuration in a real environment. At the very least the password would not be plain text.
- Happy flow implementation: implement only the functionality in controllers and services. Any error flows are handled via exceptions and exception handlers.
- Standardized error response. This provides reliability for consumers of the service to know what to expect in case of errors.
- Application configuration separation per environment.
- Containerized application for ease of deployment. Parameterized environment for re-usability.
- In a real situation the application would be built-to-fail. Any errors would be handled with either retries, separate error flows, but at the very least logged.
- Metrics exposed to allow for monitoring and alerting.
- Request tracing should be used by implementing Spring Sleuth.

## TODO
- Cover more specific error flows with tests

