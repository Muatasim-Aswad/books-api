# Books Rest API

**This Project is in progress.**

This project serves as a demonstration of building a full-fledged REST API using Spring Boot with many features.
For the sake of this purpose, entities and business logic might not reflect a complete real world scenario.

The API revolves around the following domains (Book - Author - User). See the ERD for details.

## Endpoints

See the documentation.

## Technologies

- Java 23
- Spring Boot 3.4.3
- Maven 4.0.0
- Postgres (a docker container is included for development)
- H2 Database (for testing)
- Data JPA (using Hibernate)
- Testing (MockMvc - Junit5 - Hamcrest - Mockito)
- springdoc-openapi v2.8.6

## Current Features

- Input validation
- Exception handling
- API Documentation OpenAPI/Swagger
- Logging & Auditing
- Retrieval
  - Pagination
  - Sorting (Supports multiple and nested sorts)
  - Search/Filter
- CRUD operations for entities with their relationships
- Unit & Integration Tests

## In-progress Features

- Search & Filtering
- Security:
    - Users entity
        - Roles & Permissions
            - Promote and Demote: Super Admin
            - Delete and Update: Admin
            - Create: User
            - Read: All
- Sessions
    - JWT & OAuth2
- Rate limiting
- Caching
