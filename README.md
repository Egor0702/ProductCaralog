# Product Catalog WebFlux Application

This is a reactive Product Catalog application built using **Spring WebFlux**.  
It provides a non-blocking REST API for managing products (CRUD operations), including validation, mapping, and exception handling.

## 🔧 Technologies Used

- Java 17+
- Spring Boot 3
- Spring WebFlux
- Spring Validation (Jakarta)
- Reactor (Mono / Flux)
- MapStruct (for DTO mapping)
- R2DBC database
- JUnit 5 (for tests)

## 🚀 Features

- Reactive CRUD API for products
- DTO mapping using `ProductMapper`
- Input validation with `jakarta.validation.Validator`
- Global exception handling
- Clean separation of concerns (Handler ↔ Service ↔ Repository)
- Functional routing

## 📦 Endpoints

| Method | Endpoint             | Description         |
|--------|----------------------|---------------------|
| GET    | `/products`          | Get all products    |
| GET    | `/products/{id}`     | Get product by ID   |
| POST   | `/products`          | Create a product    |
| PUT    | `/products/{id}`     | Update a product    |
| DELETE | `/products/{id}`     | Delete a product    |

All endpoints return `application/json`.