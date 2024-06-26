# employees-back

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/{pathResource}.


## Endpoints / JSON Request

> **_Contrato 1.1:_** @POST http://localhost:8080/employees

```json
{
  "gender": {
    "id": 1
  },
  "job": {
    "id": 3
  },
  "name": "Cyberia",
  "lastName": "Virtual",
  "birthDate": "1980-02-10"
}
```

> **_Contrato 1.2:_** @POST http://localhost:8080/employees/by-job-id

```json
{
  "jobId": 3
}
```


> **_Contrato 1.3:_** @GET http://localhost:8080/employees/multi-hilos

> **_Contrato 1.4:_** @POST http://localhost:8080/employees/workHours

```json
{
  "employee_id": 1,
  "start_date": "2019-01-01",
  "end_date": "2024-06-30"
}
```

> **_Contrato 1.4:_** @POST http://localhost:8080/employees/payWorkHours
```json

{
  "employee_id": 1,
  "start_date": "2019-01-01",
  "end_date": "2024-06-30"
}
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/employees-back-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence
- RESTEasy Classic JSON-B ([guide](https://quarkus.io/guides/rest-json)): JSON-B serialization support for RESTEasy Classic
- JDBC Driver - Oracle ([guide](https://quarkus.io/guides/datasource)): Connect to the Oracle database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)



### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
