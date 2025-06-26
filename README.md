# KS Management

A Spring Boot application for managing employees, offices and recruitment activities. It exposes REST endpoints secured with Spring Security and provides a small HTML/JavaScript front end.

## Prerequisites

- **Java 8** or newer (`<java.version>` is set to 1.8 in [`pom.xml`](pom.xml)).
- **Maven** 3.6+ (or use the provided `mvnw` wrapper).
- **MySQL** database. The application expects environment variables for the datasource:
    - `LOCAL_DB_URL`
    - `LOCAL_DB_USERNAME`
    - `LOCAL_DB_PASSWORD`

## Running the application

1. Clone the repository and change into the project directory.
2. Ensure the database variables above are exported in your shell.
3. Execute `./mvnw spring-boot:run` (or `mvn spring-boot:run` if Maven is installed).
4. Visit `http://localhost:8080` in your browser. A `/health-check` endpoint is available for basic status checks.
5. Upload jar file mvn clean package -P {profile} ex: mvn clean package -P dev

## Project structure

- `src/main/java/com/ks/management` – domain packages such as `employee`, `office`, `position`, `project`, `recruitment`, `report` and `security`.
- `src/main/resources` – configuration files including [`application.properties`](src/main/resources/application.properties) and profile specific overrides (`application-dev.properties`, `application-prod.properties`). SQL migrations live under `db/migration`.
- `src/main/resources/static` – HTML, JavaScript and CSS used for the UI. [`index.js`](src/main/resources/static/index.js) initializes DataTables and loads a common layout.

## Important components

- **LoggingInterceptor** – logs each request except `/health-check` and measures how long it takes.
- **SecurityConfiguration** – defines form login, logout and role based access rules.
- **Domain entities and repositories** – for example [`Employee`](src/main/java/com/ks/management/employee/Employee.java) which maps staff data via JPA annotations.
- **Service layer** – contains business logic and coordinates repository calls.
- **Flyway migrations** – scripts in `src/main/resources/db/migration` manage the schema.
- **UI layer** – HTML/JavaScript pages consume the REST endpoints. [`_layout.html`](src/main/resources/static/common/_layout.html) provides navigation and a sidebar.

## Learning pointers

- Review Spring Boot autoconfiguration and profile handling.
- Explore Spring Data JPA repositories and how services use them.
- Examine the security setup in `security/`.
- Check the Flyway migration scripts to understand how the schema evolves.
- Look into the JavaScript in `src/main/resources/static` to see how DataTables are populated.
