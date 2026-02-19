# Employee Management API

A production-style REST API built with Spring Boot to manage employees and departments.

This project demonstrates backend engineering principles including layered architecture, structured exception handling, validation, logging discipline, and RESTful best practices.  

Developed as part of structured backend practice (Bucket 3 phase).

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- H2 Database

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
2. Run:
   mvn spring-boot:run
3. Access API at:
   http://localhost:8080
   
## Future Improvements

- Unit testing with JUnit & Mockito
- CI/CD pipeline with GitHub Actions
- Docker containerization
- Security with Spring Security