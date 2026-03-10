# Camper Park Management Tool 🚐

A backend application for managing reservations, guests, and pricing in a camper park.

## Key Functionalities

### 1. Flexible Pricing Model
- **Base Prices:** Defined globally per camper place type (Standard, VIP, etc.).
- **Price Overrides:** Ability to set custom prices for specific camper places that take precedence over the base type price.
- **Automatic Fallback:** System automatically uses the type's price if no specific override is defined.

### 2. Modern Technical Foundation
- **Clean DI:** Fully based on constructor injection with Lombok `@RequiredArgsConstructor`.
- **Generic CRUD:** A base service implementation that handles standard database operations to keep the code DRY.
- **JWT Security:** Authentication using RSA-signed JSON Web Tokens.

### 3. Database Management
- **Liquibase:** Version-controlled database schema.
- **Context-based setup:**
  - `prod` context: schema and migrations.
  - `dev` context: automatic seeding of 50 guests and 100 sample reservations for testing.

## Tech Stack
- **Core:** Java 25, Spring Boot 4
- **Security:** Spring Security (OAuth2 Resource Server)
- **Database:** MySQL 8.0, Hibernate
- **Migrations:** Liquibase
- **Testing:** JUnit 5, Testcontainers (MySQL)

## Quick Start

### Prerequisites
- Docker
- JDK 25
- Maven

### Running the app
Use the `dev` profile to automatically create the schema and load demo data:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testing
```bash
mvn test
```

## API Docs
Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

---
*Status: Work in Progress.*
