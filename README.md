# Books Rest API

** This Readme is not complete yet. **

This project serves as a demonstration of building a REST API using Spring Boot including different features.
For the sake of this purpose, entities and business logic does not reflect a full-fledged application or a complete real world scenario.

The API revolves around the following domains:
- Book
- Author
where a book must have at least one author.

## Technologies
- Java 23
- Spring Boot 3.4.3
- Maven 4.0.0
- Postgres (a docker container is included for local development)
- H2 Database (for testing)
- Data JPA (using Hibernate)

## Current Features
- Manages CRUD operations for Book and Author entities with their relationships
- Input validation
- Unit and Integration tests for the services
- Exception handling

## Future Features
- API Documentation according to OpenAPI
- Pagination
- Search & Filtering
- Logging
- Security:
  - Users entity
  - JWT & OAuth2
  - Sessions
  - Roles & Permissions
    - Promote and Demote: Super Admin
    - Delete and Update: Admin
    - Create: User
    - Read: All
- Rate limiting
- Caching
