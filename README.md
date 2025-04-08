![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

# Books REST API

A Spring Boot REST API demonstration for managing books and authors. This project showcases modern Java backend
development practices with a focus on clean architecture, best practices, and including different features.

> [!NOTE]
> This repository is not final, and some parts of it are still a work in progress.

---

## 📋 Overview

This API provides complete CRUD operations for book and author resources with advanced querying capabilities, thorough
validation, and comprehensive documentation.

---

## 📑 Table of Contents

- [📋 Overview](#-overview)
- [🚀 Tech Stack](#-tech-stack)
- [✨ Features](#-features)
- [🔍 API Documentation](#-api-documentation)
- [🛠️ Getting Started](#%EF%B8%8F-getting-started)
- [📁 Project Structure](#-project-structure)
- [🏗️ Design](#%EF%B8%8F-design)
- [🔜 Roadmap](#-roadmap)
- [📄 License](#-license)

---

## 🚀 Tech Stack

- **Java 23**
- **Spring Boot 3.4.3**
- **Maven 4.0.0**
- **Database**
    - PostgreSQL (containerized for development)
    - H2 (for testing)
- **ORM**: Spring Data JPA with Hibernate
- **Documentation**: OpenAPI/Swagger (springdoc-openapi v2.8.5)
- **Testing**: JUnit 5, MockMvc, Hamcrest, Mockito
- **API Features**: Validation, Exception Handling, Logging & Auditing, Caching

---

## ✨ Features

### Core Functionality

- Complete CRUD operations for books and authors
- Comprehensive input validation
- Global exception handling
- Request/response logging and auditing

### Advanced Querying

- Pagination support
- Multi-field and nested sorting
- Flexible search and filtering

### Developer Experience

- Comprehensive API documentation with Swagger UI
- Containerized development environment
- Extensive test coverage (unit & integration)

---

## 🔍 API Documentation

API documentation is available via Swagger UI when the application is running on default local port:
http://localhost:8080/swagger-ui/index.html

---

## 🛠️ Getting Started

### Prerequisites

- Java 23 or higher
- Maven 4.0.0 or higher
- Docker and Docker Compose (for development database)

### Running Locally

1. Clone the repository
   ```bash
   git clone <repository-url>
   cd books
    ```
2. Build the application
   ```bash
   mvn clean install
    ```
3. Run the application while docker desktop is running
   ```bash
   mvn spring-boot:run
    ```

The API will be available at http://localhost:8080

### Testing the Application

4. Testing the application
   ```bash
   mvn test
    ```

### Test Standards

The project has unit, integration, web layer tests with a high degree of coverage.
For detailed test standards and guidelines, refer to
the [Test Standards and Guidelines](src/test/java/testStandards.md).

---

## 📁 Project Structure

The project is organized by feature domains (book, author, user) then the technical layers (controller - service ...).

### Core Structure

```plaintext
src/main/java/com/asim/books/
├── BooksApplication.java            # Application entry point
├── common/                          # Shared utilities and base components
├── domain/                          # Feature domains (book, author, etc.)
│   ├── author/                      # Author domain components
│   └── book/                        # Book domain components
└── infrastructure/                  # Cross-cutting concerns
    ├── config/                      # Application configuration
    ├── exception/                   # Global exception handling
    └── logging/                     # Logging infrastructure
```

### Domain Organization

Each domain follows a consistent structure (Author Example):

```plaintext
domain/author/
├── controller/                      # REST API endpoints and API annotations
│   ├── AuthorApi.java               # Interface defining the API contract
│   ├── AuthorController.java        # Implementation of API endpoints
│   └── annotation/                  # OpenAPI/Swagger annotations
├── model/                           # Domain models
│   ├── dto/                         # Data Transfer Objects
│   ├── entity/                      # JPA entities
│   └── mapper/                      # Object mappers
├── repository/                      # Data access layer
│   └── AuthorRepository.java        # Spring Data JPA repository
├── service/                         # Business logic
│   ├── AuthorService.java           # Service interface
│   └── AuthorServiceImpl.java       # Service implementation
└── facade/                          # Orchestration layer
    ├── AuthorFacade.java            # Interface for business operations
    └── AuthorFacadeImpl.java        # Implementation of business logic


domain/book/
└── gateway/                         # Gateway layer for using external services
    ├── AuthorGateway.java           # Interface for external service interactions
    └── AuthorGatewayImpl.java       # Implementation of external service interactions
```

The facade and gateway layers are designed to manage internal interactions between domain services, providing a clear
separation of concerns.
The facade provides an API to define and restrict access to its services.
The gateway is used by client to restrict the usage of a facade.

### Common Components

The common directory contains shared components, utilities, and base classes used across the app.

```plaintext
common/
├── annotation/                      # Custom annotations. e.g. for documentation and validation
├── exception/                       # Custom exception types
├── model/                           # Common model components. e.g. EntityDtoMapper Base class
└── util/                            # Utility classes
    ├── ContradictionUtils.java      # Custom type of equality checker
    ├── ReflectionUtils.java         # Reflection-based utilities
    └── SortUtils.java               # Sorting query parameter handling
```

### Infrastructure

The infrastructure directory contains cross-cutting concerns such as configuration, exception handling, and logging.

```plaintext
infrastructure/
├── config/                          # Application configuration
│   ├── JpaConfig.java               # Database configuration
│   └── WebMvcConfig.java            # MVC configuration
├── exception/                       # Global exception handling
│   └── GlobalExceptionHandler.java  # Centralized exception handling
└── logging/                         # Logging components
    └── RequestLoggingInterceptor.java  # HTTP request/response logging
```

### Modularization

A module with only one possible user should be next to it. If the module has the potential to be reused, it should be
in the root directory of potential users.
e.g. `annotations` can be in the `/common/annotation` directory, but if not foreseen to be used out of `/domain/author/controller`, it
should be in the directory `... /controller/annotation`.

---

## 🏗️ Design

### Entity Relationship Diagram (ERD)

For live view: [ERD](https://lucid.app/lucidchart/9c1f16ff-2505-4885-bc86-c9f241fdff4f/edit?viewport_loc=-1252%2C-1366%2C3727%2C1780%2C0_0&invitationId=inv_e1cb1f5d-6387-4fc5-a183-bd0aacc8608a)


Below is the ERD for the Books REST API, illustrating the relationships between the entities:


![Books ERD](https://github.com/user-attachments/assets/b12f4eb5-bc1d-4ff6-a1da-16e944e4f95b)

### Principles

- Even within the current monolithic design, the services and domains should have minimal interactions to ensure a clear
  separation of concerns, and maintainability.
- The authentication service should not interact directly with the domain services, so that it has the potential to be
  independently
  deployed and scaled. The communication with it should be done asynchronously as possible.
- The interactions between domain services should be managed through a well-defined API contract for loose coupling.

### Auth Management

Regardless of the auth strategy, the auth lifecycle should be managed using **JWT tokens** to achieve a high degree of
statelessness.

- **Issuance**: The auth service issues JWT tokens (Access, Refresh) upon successful authentication and refreshes the
  access token as requested by the client.
- **Revocation**: The auth service marks both tokens as invalid on a logout request.
- **Validation**: Access token validation is performed explicitly by the domain services.

---

## 🔜 Roadmap

### Security Implementation

#### User Management

- Credentials management: auth service
- Profile management: domain services

#### Role-Based Access Control

- **Admin**: Editor + Promote/Demote User
- **Editor**: Contributor + Update/Delete Resources
- **Contributor (user)**: Viewer + Create Resources
- **Viewer (no registration)**: Read-Only

#### Authentication

- JWT Authentication
- OAuth2 Integration

#### API Protection

- Rate Limiting

### Deployment

- CI/CD pipeline setup
- Dockerize the application
- Deploy to a cloud provider
- Kubernetes deployment

### Caching

- Use Redis for caching instead of in-memory caching

### Microservices

- Split the application into microservices:
    - domain services
    - Auth service

### Additional
- Versioning format of the endpoints

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
