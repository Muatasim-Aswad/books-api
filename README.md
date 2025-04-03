# Books REST API

A Spring Boot REST API demonstration for managing books and authors. This project showcases modern Java backend
development practices with a focus on clean architecture, best practices, and including different features.

## 📋 Overview

This API provides complete CRUD operations for book and author resources with advanced querying capabilities, thorough
validation, and comprehensive documentation.

## 📑 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
    - [Core Functionality](#core-functionality)
    - [Advanced Querying](#advanced-querying)
    - [Developer Experience](#developer-experience)
- [API Documentation](#-api-documentation)
- [Getting Started](#-getting-started)
    - [Prerequisites](#prerequisites)
    - [Running Locally](#running-locally)
- [Roadmap](#-roadmap)
    - [Security Implementation](#security-implementation)
    - [Deployment](#deployment)
    - [Caching](#caching)
    - [Microservices](#microservices)
- [Project Structure](#-project-structure)
    - [Core Structure](#core-structure)
    - [Domain Organization](#domain-organization)
    - [Common Components](#common-components)
    - [Infrastructure](#infrastructure)
- [License](#-license)

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

## 🔍 API Documentation

API documentation is available via Swagger UI when the application is running:
http://localhost:8080/swagger-ui/index.html

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

The api will be available at http://localhost:8080

4. Testing the application
   ```bash
   mvn test
    ```

## 📁 Project Structure

The project follows a domain-driven design, organized by feature domains then the technical layers.

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

## 🔜 Roadmap

### Security Implementation

- **User Management**
    - Credentials management: auth service
    - Profile management: business service
- **Role-Based Access Control**
    - **Admin**: Editor + Promote/Demote User
    - **Editor**: Contributor + Update/Delete Resources
    - **Contributor**: Viewer + Create Resources
    - **Viewer**: Read-Only
- **Authentication**
    - JWT Authentication
    - OAuth2 Integration
- **API Protection**
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
    - Business service
    - Auth service

## 📄 License

[License information]