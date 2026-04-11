# Wedding RSVP App API

A RESTful API backend for a Wedding RSVP application. Built with **Spring Boot 3** and **Java 17**, this API serves a **Next.js** frontend, allowing wedding guests to search for their RSVP group, confirm attendance, and specify dietary restrictions and food allergies.

The application was previously hosted on an **Amazon EC2** instance and connected to an **Amazon RDS** PostgreSQL database.

---

## Tech Stack

| Layer            | Technology                                      |
| ---------------- | ----------------------------------------------- |
| Language         | Java 17                                         |
| Framework        | Spring Boot 3.1                                 |
| Database         | PostgreSQL (Amazon RDS)                         |
| ORM              | Spring Data JPA / Hibernate                     |
| Migrations       | Liquibase                                       |
| Auth             | API Key authentication (Spring Security)        |
| Build Tool       | Gradle                                          |
| Testing          | Spock (Groovy), Testcontainers (PostgreSQL)     |
| Containerization | Docker (Azul Zulu OpenJDK 17)                   |
| Hosting          | Amazon EC2                                      |

---

## Features

- **RSVP Groups** – Guests are organized into groups (e.g., a family or couple). Each group has a lead, an email, and a set of individual RSVPs.
- **Search by Name** – Look up RSVP groups by a guest's name.
- **Update Attendance** – Guests can confirm or decline their attendance.
- **Dietary Restrictions & Food Allergies** – Each guest can specify dietary restrictions (e.g., no red meat, no pork, no dairy) and food allergies (e.g., peanuts, tree nuts, soy).
- **Group Management** – Groups with the `modifyGroup` flag can add or remove members.
- **API Key Security** – All endpoints are secured with API key–based authentication.

---

## API Endpoints

All endpoints are under `/api` and require a valid API key.

### RSVP Groups

| Method | Endpoint                          | Description                                        |
| ------ | --------------------------------- | -------------------------------------------------- |
| GET    | `/api/rsvp-groups/{id}`           | Get an RSVP group by ID                            |
| GET    | `/api/rsvp-groups-by-name/{name}` | Search for RSVP groups by a guest's name           |
| POST   | `/api/update-rsvp-groups`         | Update email addresses for RSVP groups             |
| POST   | `/api/update-rsvp-and-rsvp-groups`| Update RSVP group emails and guest attendance/diet |

### RSVPs

| Method | Endpoint                  | Description                                       |
| ------ | ------------------------- | ------------------------------------------------- |
| GET    | `/api/rsvps/{id}`         | Get an individual RSVP by ID                      |
| POST   | `/api/update-rsvps`       | Update attending status and food restrictions      |
| PUT    | `/api/rsvps/{groupId}`    | Add/update RSVPs in a group                       |
| DELETE | `/api/rsvps/{groupId}`    | Remove RSVPs from a group                         |

---

## Prerequisites

- **Java 17**
- **Docker** (for containerized runs and Testcontainers-based tests)
- **PostgreSQL** (for local development without Docker)

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/wedding-rsvp-app-api.git
cd wedding-rsvp-app-api
```

### 2. Run PostgreSQL locally

Start a local PostgreSQL instance (e.g., via Docker):

```bash
docker run -d \
  --name wedding-rsvp-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=mypass \
  -e POSTGRES_DB=postgres \
  -p 5432:5432 \
  postgres:14
```

### 3. Run the application

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

The API will start on **http://localhost:8080**.

### 4. Run tests

Tests use **Spock** + **Testcontainers** (spins up a PostgreSQL container automatically):

```bash
./gradlew test
```

---

## Running with Docker

You can either use the DevContainer setup or run `docker compose -f .devcontainer/docker-compose.yml up -d` to start both the API and PostgreSQL together.

Or, if you only wanted to run the API container and connect it to a PostgreSQL instance, use:

```bash
# Build the image
docker build -t wedding-rsvp-app-api .

# Run the container (macOS)
docker run -d \
  --name wedding-rsvp-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres \
  -e SPRING_DATASOURCE_USERNAME=myuser \
  -e SPRING_DATASOURCE_PASSWORD=mypass \
  -e RSVP_API_CLIENT_KEY=<your-api-key> \
  -e SPRING_SQL_INIT_MODE=always \
  wedding-rsvp-app-api
```

> **Note:** Use `host.docker.internal` instead of `localhost` in the datasource URL of postgres instance is running in 
> docker locally.
> Inside a Docker container, `localhost` refers to the container itself — not the host machine.
> On macOS and Windows, `host.docker.internal` resolves to the host, where your PostgreSQL container's port is mapped.
> On Linux, add `--add-host=host.docker.internal:host-gateway` to the `docker run` command.

### Test the API

```bash
curl -s -H "X-API-KEY: key" http://localhost:8080/api/rsvp-groups-by-name/John
```

---

## Database Migrations

Database schema changes are managed by **Liquibase**. Migration scripts are located in:

```
src/main/resources/db/changelog/sql/
```

Migrations run automatically on application startup.

---

## Project Structure

```
src/main/java/com/dillon/weddingrsvpapi/
├── auth/            # API key authentication & Spring Security config
├── controller/      # REST controllers (RsvpController, RsvpGroupController)
├── db/              # Spring Data JPA repositories
├── dto/             # Entity classes & enums (Rsvp, RsvpGroup, DietaryRestriction, FoodAllergies)
├── exception/       # Custom exceptions
├── service/         # Business logic services
└── util/            # Error handling utilities

src/main/resources/
├── application-local.properties   # Local development config
├── application-test.properties    # Test config
├── data.sql                       # Seed data for local development
└── db/changelog/                  # Liquibase migration scripts

src/test/groovy/                   # Spock test specifications
```

