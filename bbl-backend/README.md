# BBL Backend

A Spring Boot backend application for the BBL (Bangkok Bank Learning) project.

## Overview

This is a Java Spring Boot application that serves as the backend API for the BBL system. It's built using Spring Boot 3.5.6 and Java 21.

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.6
- **Build Tool**: Maven
- **Package**: JAR

## Project Structure

```
bbl-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/th/bbl/backend/
│   │   │       └── BblBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/th/bbl/backend/
│               └── BblBackendApplicationTests.java
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper)

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd bbl-test/bbl-backend
```

### Build the Application

Using Maven wrapper (recommended):

```bash
./mvnw clean compile
```

Or using Maven directly:

```bash
mvn clean compile
```

### Run the Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or using Maven directly:

```bash
mvn spring-boot:run
```

The application will start on the default port (usually 8080).

### Run Tests

```bash
./mvnw test
```

## Building for Production

### Create JAR File

```bash
./mvnw clean package
```

The JAR file will be created in the `target/` directory.

### Run the JAR File

```bash
java -jar target/bbl-backend-0.0.1-SNAPSHOT.jar
```

## Configuration

Application configuration can be found in `src/main/resources/application.properties`.

## API Documentation

Once the application is running, you can access:

- Application: `http://localhost:8080`
- Health Check: `http://localhost:8080/actuator/health` (if actuator is enabled)

## Development

### Code Style

This project follows standard Java coding conventions and Spring Boot best practices.

### Adding Dependencies

Add new dependencies to the `pom.xml` file and run:

```bash
./mvnw dependency:resolve
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write tests for your changes
5. Run the test suite
6. Submit a pull request

## License

This project is part of a coding test for Bangkok Bank Learning (BBL).

## Support

For questions or issues, please contact the development team.