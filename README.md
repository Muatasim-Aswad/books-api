![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![gRPC](https://img.shields.io/badge/gRPC-4285F4?style=for-the-badge&logo=grpc&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-6BA539?style=for-the-badge&logo=openapi-initiative&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

# Books API Platform

A modular Java backend platform for managing books, authors, and user authentication, built with Spring Boot, gRPC, and
PostgreSQL. The project demonstrates modern backend practices, clean architecture, and microservice design.

> [!NOTE]
> This repository is under active development. Some features may be incomplete or subject to change.

---

## 📑 Table of Contents

- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Design & Architecture](#-design--architecture)
- [License](#-license)

---

## 🚀 Tech Stack

- **Java 21+**
- **Spring Boot 3.4.x**
- **Maven**
- **gRPC** (with protobuf)
- **PostgreSQL** (main DB)
- **H2** (for testing)
- **Redis** (for caching, via Spring Data Redis)
- **OpenAPI/Swagger** (API docs)
- **JUnit 5, Hamcrest, Mockito, MockMVC** (testing)
- **Docker & Docker Compose** (for local development)

---

## ✨ Features

- Modular multi-service architecture (auth, business, shared)
- gRPC-based internal communication
- RESTful APIs for external clients
- JWT-based authentication and session management
- Role-based access control following RBAC principles::
  - Viewer/Unauthenticated: Can access public resources (read-only)
  - Contributor: Can add new resources
  - Editor: Can add, update, and delete resources
  - Admin: Can manage permissions by promoting or demoting users
- CRUD for books and authors with advanced querying (pagination, filtering, nested sorting)
- Input validation and global exception handling
- Request/response logging and auditing
- Caching (in-memory and Redis)
- Comprehensive API documentation (Swagger UI)
- Containerized development and test environments
- Unit and integration tests

---

## 🛠️ Getting Started

### Prerequisites

- Java 21 or higher
- Maven 4.0.0 or higher
- Docker & Docker Compose

### Build All Modules

```bash
mvn clean install
```

### Run Services

Each service can be run independently. For local development, ensure Docker is running for PostgreSQL/Redis.

#### Start auth-service

```bash
cd auth-service
mvn spring-boot:run
```

#### Start business-service

```bash
cd business-service
mvn spring-boot:run
```

#### Additional

To complete setting up the `admin` role in the database, run the following script:

```bash
chmod +x update_admin_role.sh
./update_admin_role.sh
```

#### (Optional) Start with Docker Compose

Each service has its own `compose.yaml` for DB dependencies.

---

## 🔍 API Documentation

- **REST API**: Available at `http://localhost:8081/swagger-ui` (auth-service) and `http://localhost:8082/swagger-ui` (
  business-service)
- **gRPC API**: See proto files in `grpc-shared/src/main/proto`

---

## 📁 Project Structure

```
books-api-repo/
├── auth-service/         # Authentication microservice (gRPC client + REST)
├── business-service/     # Business logic microservice (gRPC server + REST)
├── grpc-shared/          # Shared gRPC proto definitions and generated code
└── ...
```

- Each service is a standalone Spring Boot app with its own dependencies and configuration.
- `grpc-shared` is imported as a dependency in both services for gRPC contract sharing.

```plaintext
src/main/java/com/y/x/
├── XApplication.java                # Application entry point
├── common/                          # Shared utilities and base components
├── domain/                          # Feature domains (book, author, etc.)
└── infrastructure/                  # Cross-cutting concerns
```

```plaintext
domain/x/
├── controller/                      # REST API endpoints and API annotations
│   ├── XApi.java                    # Interface defining the API specification
│   └── XController.java             # Implementation of API specification
├── model/                           # Domain models
│   ├── dto/                         # Data Transfer Objects
│   ├── entity/                      # JPA entities
│   └── mapper/                      # Object mappers
├── repository/                      # Data access layer
├── service/                         # Business logic
├── facade/                          # Exposes internal services to other local services
└── gateway/                         # Enable external local services for internal consumption
```

- While business-service is structured feature-wise then technical, auth-service is structured first by technical layer,
  e.g., `core/controllers/XController.java`. This decision is made according to the service's complexity and
  requirements.

---

## 🏗️ Design & Architecture

The system consists of the following main services:

- **auth-service**: Handles authentication, JWT issuance, and user credentials.
- **business-service**: Manages domain logic, books/authors/userProfiles CRUD, and authorization.

Services communicate internally using gRPC and expose REST API for clients.

<img width="554" alt="system design" src="https://github.com/user-attachments/assets/665183e5-58ef-4572-a5d7-d41b6fea010c" />

<img width="708" alt="erd" src="https://github.com/user-attachments/assets/ea1e3755-6799-4bc2-ac27-4429725192cc" />

---

- **Separation of Concerns and Minimal Dependencies**: Services whether local or global should have minimal coupling.
  Any interaction` between services is done through well-defined interfaces.
- **gRPC for Internal APIs**: Fast, strongly typed communication between services.
- **REST for External APIs**: Standard HTTP/JSON for client interaction.
- **JWT Auth**: Stateless authentication, where issuance and refreshing is done by the auth service, while validating
  access tokens is in the consumer service.
- **Role-based Access Control**: Different permissions for users.
- **Clean Architecture**: Feature-based package structure, clear layering (controller, service, repository, etc).

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
