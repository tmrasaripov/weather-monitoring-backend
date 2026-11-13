# Weather Monitoring & Alert System Backend

A robust, enterprise-grade backend application built with **Java 17** and **Spring Boot 3.2+** designed for local weather monitoring, forecasting, meteorological alerts, user reports, and subscriptions.

---

## 🏗️ Architecture & Tech Stack

- **Java 17** / **Spring Boot 3.2+**
- **Spring Web (REST API)**
- **Spring Data JPA** (Hibernate)
- **Spring Security** with **JWT (JSON Web Token)** stateless authentication
- **H2 Database** (embedded, in-memory for zero-config out-of-the-box running) + commented-out PostgreSQL profile in `application.yml`
- **Lombok** (for boilerplate reduction)
- **Springdoc OpenAPI (Swagger UI)** for interactive API documentation
- **Global Exception Handling** (`@RestControllerAdvice`)

---

## 👥 User Roles & Permissions

1. **`ROLE_USER`**:
   - View current weather and weather forecasts by location.
   - Subscribe and unsubscribe from location weather alerts.
   - View active severe weather alerts.
   - Submit user weather reports (actual weather observations).
2. **`ROLE_METEOROLOGIST`**:
   - All `USER` capabilities.
   - Add/update weather station measurements (`/api/weather`).
   - Create weather forecasts (`/api/forecasts`).
   - Publish official weather and storm alerts (`/api/alerts`).
   - Moderate user weather reports (approve/reject).
3. **`ROLE_ADMIN`**:
   - Full system access.
   - Manage locations and weather stations (CRUD).
   - Manage users, roles, and administrative tasks.

---

## 🚀 Getting Started & Running the App

### Prerequisites
- Java 17 installed and configured.
- Maven (or use included `mvnw` / `mvnw.cmd`).

### Build & Run
```bash
# Clone repository
git clone https://github.com/tmrasaripov/weather-monitoring-backend.git
cd weather-monitoring-backend

# Compile and package
mvn clean package

# Run the Spring Boot application
mvn spring-boot:run
```

The application will start on **`http://localhost:8080`**.

---

## 📚 API Documentation & Swagger UI

Once the application is running, open your browser to explore and test endpoints interactively via Swagger UI:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:weatherdb`, Username: `sa`, Password: `password`)

---

## 🧪 Default Test Accounts (Initialized via DataInitializer)

Upon startup, the database is automatically seeded with initial locations, measurements, forecasts, alerts, and 3 test accounts:

| Username | Password | Role | Description |
|---|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN` | System Administrator |
| `meteo` | `meteo123` | `ROLE_METEOROLOGIST` | Meteorologist / Station Operator |
| `user` | `user123` | `ROLE_USER` | Regular End User |

---

## 📝 Example cURL & Postman Requests

### 1. Authenticate / Login (Get JWT Token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "user123"}'
```
Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 3,
  "username": "user",
  "email": "user@weather.com",
  "role": "ROLE_USER"
}
```

### 2. Get All Locations (Public)
```bash
curl -X GET http://localhost:8080/api/locations
```

### 3. Get Latest Weather for Location ID 1 (Moscow) (Public)
```bash
curl -X GET http://localhost:8080/api/weather/location/1/latest
```

### 4. Subscribe to Location ID 2 (Saint Petersburg) (User)
```bash
curl -X POST http://localhost:8080/api/subscriptions/location/2 \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

### 5. Create Weather Record (Meteorologist / Admin)
```bash
curl -X POST http://localhost:8080/api/weather \
  -H "Authorization: Bearer <METEOROLOGIST_JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "locationId": 1,
    "temperature": 19.5,
    "humidity": 60.0,
    "pressure": 1012.0,
    "windSpeed": 5.1,
    "conditionText": "Sunny"
  }'
```

---

## 🗄️ PostgreSQL Configuration (Optional)

To switch from the default in-memory H2 database to PostgreSQL, open `src/main/resources/application.yml`, uncomment the PostgreSQL profile section, configure your database credentials, and activate the `postgres` spring profile (e.g., `-Dspring.profiles.active=postgres`).
