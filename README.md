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

| Method | Endpoint                   | Description                        |
|--------|----------------------------|------------------------------------|
| GET    | `/products?after=0&size=5` | Get products (supports pagination) |
| GET    | `/products/{id}`           | Get product by ID                  |
| POST   | `/products`                | Create a product                   |
| PUT    | `/products/{id}`           | Update a product                   |
| DELETE | `/products/{id}`           | Delete a product                   |

All endpoints return `application/json`.

## 🔔 Real-Time Updates via WebSocket

The application supports real-time product updates using **WebSocket**.

Whenever a new product is created or updated, connected clients will automatically receive a notification with the product's data in JSON format.

### 📡 WebSocket Endpoint

- `ws://localhost:8080/ws/products` — subscribes the client to product updates.

### 🔁 How It Works

- Clients establish a WebSocket connection.
- The server emits updates to all connected clients via a shared `Sinks.Many<Product>` stream.
- Updates are triggered when a new product is created or updated.

### 🧪 Example (Using JavaScript in Browser Console)

```javascript
const socket = new WebSocket("ws://localhost:8080/ws/products");

socket.onmessage = (event) => {
  const product = JSON.parse(event.data);
  console.log("Received product update:", product);
};