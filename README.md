# Books REST API

A robust Spring Boot REST API demonstration for managing books and authors. This project showcases modern Java backend
development practices with a focus on clean architecture, comprehensive testing, and developer-friendly features.

## 📋 Overview

This API provides complete CRUD operations for book and author resources with advanced querying capabilities, thorough
validation, and comprehensive documentation.

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

## 🔜 Roadmap

### Security Implementation

- **User Management**
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
    - Request Throttling

## 📄 License

[License information]