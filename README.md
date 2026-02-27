# Employee Management API

[![CI Pipeline](https://github.com/am-khant/employee-management-api/actions/workflows/ci.yml/badge.svg)](https://github.com/am-khant/employee-management-api/actions/workflows/ci.yml)

A production-style REST API built with Spring Boot to manage employees and departments.

This project demonstrates backend engineering principles including layered architecture, structured exception handling, validation, logging discipline, and RESTful best practices.  

Developed as part of structured backend practice.

---

## Tech Stack

- Java 17
- Spring Boot 3.3.5
- Spring Data JPA
- Hibernate
- Maven
- H2 Database (for testing)
- Junit 5
- MockMvc
- GitHub Actions (CI)

---

## Architecture

The project follows a layered architecture:

Controller → Service → Repository → Database

- **Controllers** handle HTTP requests and responses.
- **Services** contain business logic and application rules.
- **Repositories** manage database interactions using Spring Data JPA.
- **DTOs** are used to separate request/response models from entities.
- **Mappers** convert between entities and DTOs.
- **Global exception handling** provides structured API error responses.
- **Validation** ensures request integrity using Jakarta Bean Validation.
- **Logging** is implemented with appropriate log levels (INFO, WARN, ERROR).

---

## Features

- CRUD operations for Employees
- CRUD operations for Departments
- Pagination support for Employees
- Structured validation with detailed field-level error responses
- Custom exception handling (400, 404, 409, 500)
- Centralized API error response model (`ApiError`)
- Logging discipline with proper severity levels
- Full test pyramid:
   - Unit tests
   - Controller slice tests
   - Integration tests
- Automated CI pipeline on push to main

---

## Testing Strategy

This project implements a structured testing pyramid:

- **Unit Tests** – Business logic validation
- **Slice Tests (@WebMvcTest)** – Controller layer verification
- **Integration Tests (@SpringBootTest)** – End-to-end system testing with H2
- **CI Pipeline** – Runs mvn clean verify on every push

All tests are executed automatically via GitHub Actions.

---
## Error Handling

The API returns structured JSON responses for:

- **400 Bad Request** – Validation errors
- **404 Not Found** – Resource not found
- **409 Conflict** – Business rule violations
- **500 Internal Server Error** – Unexpected system failures

All error responses follow a consistent `ApiError` structure.

---

## How to Run

1. Clone the repository
2. Navigate to the project directory
3. Run:
   mvn spring-boot:run
4. Access API at:
   http://localhost:8080
   
---

## API Documentation

Interactive API documentation is available via Swagger UI.

After running the application, access:

http://localhost:8080/swagger-ui/index.html

The documentation allows testing endpoints directly from the browser and provides request/response schemas.

---
   
## Continuous Integration  

This project uses GitHub Actions for Continuous Integration.

On every push to main:

- The project is built from scratch
- All tests are executed
- The build fails if any test fails

This ensures code stability and production readiness.

---
   
## Future Improvements

- Docker containerization
- Security with Spring Security
- Continuous Deployment (CD) pipeline