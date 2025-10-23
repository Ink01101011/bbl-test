# BBL Test Project

A full-stack application consisting of a Spring Boot backend and a Next.js frontend for the Bangkok Bank Learning (BBL) coding test.

## Project Overview

This repository contains two main applications:

- **bbl-backend**: A Java Spring Boot REST API
- **bbl-web-app**: A React/Next.js web application

## Architecture

```
┌─────────────────┐       HTTP/REST       ┌─────────────────┐
│   bbl-web-app   │ ────────────────────► │   bbl-backend   │
│   (Frontend)    │                       │   (Backend)     │
│                 │                       │                 │
│ • Next.js 16    │                       │ • Spring Boot   │
│ • React 19      │                       │ • Java 21       │
│ • TypeScript    │                       │ • Maven         │
│ • Tailwind CSS  │                       │                 │
└─────────────────┘                       └─────────────────┘
```

## Quick Start

### Prerequisites

- **Java 21** or higher
- **Node.js 20** or higher
- **Maven 3.6+** (or use included wrapper)
- **npm/yarn/pnpm/bun**
- **Docker & Docker Compose** (optional, for containerized deployment)

### Option 1: Local Development

#### 1. Clone the Repository

```bash
git clone <repository-url>
cd bbl-test
```

#### 2. Start the Backend

```bash
cd bbl-backend
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

#### 3. Start the Frontend

In a new terminal:

```bash
cd bbl-web-app
npm install
npm run dev
```

The frontend will start on `http://localhost:3000`

### Option 2: Docker Deployment

#### Quick Start with Docker

```bash
# Build and start all services
./docker.sh up

# Or use docker-compose directly
docker-compose up --build
```

#### Available Docker Commands

```bash
# Build all images
./docker.sh build

# Start production services
./docker.sh up

# Start development services (with hot reload)
./docker.sh dev

# View logs
./docker.sh logs

# Stop all services
./docker.sh down

# Clean up everything
./docker.sh clean

# Run only backend
./docker.sh backend

# Run only frontend
./docker.sh frontend
```

#### Access Points

- **Frontend**: `http://localhost:3000`
- **Backend**: `http://localhost:8080`
- **Backend Debug** (dev mode): `localhost:5005`

## Project Structure

```
bbl-test/
├── bbl-backend/           # Spring Boot backend
│   ├── src/
│   │   ├── main/java/
│   │   └── test/java/
│   ├── pom.xml
│   └── README.md
├── bbl-web-app/           # Next.js frontend
│   ├── app/
│   ├── public/
│   ├── package.json
│   └── README.md
└── README.md              # This file
```

## Technology Stack

### Backend (bbl-backend)
- **Language**: Java 21
- **Framework**: Spring Boot 3.5.6
- **Build Tool**: Maven
- **Testing**: JUnit, Spring Boot Test

### Frontend (bbl-web-app)
- **Framework**: Next.js 16.0.0
- **Language**: TypeScript 5
- **UI Library**: React 19.2.0
- **Styling**: Tailwind CSS 4
- **Linting**: ESLint

## Development Workflow

### Backend Development

1. Navigate to backend directory: `cd bbl-backend`
2. Make your changes
3. Run tests: `./mvnw test`
4. Start application: `./mvnw spring-boot:run`

### Frontend Development

1. Navigate to frontend directory: `cd bbl-web-app`
2. Make your changes
3. Run linting: `npm run lint`
4. Start development server: `npm run dev`

## Building for Production

### Local Build

#### Backend

```bash
cd bbl-backend
./mvnw clean package
java -jar target/bbl-backend-0.0.1-SNAPSHOT.jar
```

#### Frontend

```bash
cd bbl-web-app
npm run build
npm run start
```

### Docker Build

#### Production Deployment

```bash
# Build and start all services
docker-compose up --build -d

# View running containers
docker-compose ps

# View logs
docker-compose logs -f
```

#### Development with Docker

```bash
# Start development environment with hot reload
docker-compose -f docker-compose.dev.yml up --build

# Or use the helper script
./docker.sh dev
```

#### Individual Service Deployment

```bash
# Backend only
docker-compose up --build -d bbl-backend

# Frontend only
docker-compose up --build -d bbl-web-app
```

## Environment Configuration

### Backend Configuration

Edit `bbl-backend/src/main/resources/application.properties`:

```properties
# Server configuration
server.port=8080

# Database configuration (if needed)
# spring.datasource.url=jdbc:h2:mem:testdb
```

### Frontend Configuration

Create `bbl-web-app/.env.local`:

```env
# API Configuration
NEXT_PUBLIC_API_URL=http://localhost:8080

# Other environment variables
# NEXT_PUBLIC_APP_ENV=development
```

## API Documentation

### Backend Endpoints

Once the backend is running, available endpoints:

- Health Check: `GET http://localhost:8080/actuator/health`
- API Base URL: `http://localhost:8080/api/`

### Frontend Pages

- Home Page: `http://localhost:3000`
- (Additional pages will be documented as they are developed)

## Testing

### Backend Tests

```bash
cd bbl-backend
./mvnw test
```

### Frontend Tests

```bash
cd bbl-web-app
npm test  # (when test scripts are added)
```

## Code Quality

### Backend
- Follow Java coding standards
- Use Spring Boot best practices
- Maintain test coverage

### Frontend
- TypeScript strict mode enabled
- ESLint configuration enforced
- Consistent component patterns

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Make your changes in the appropriate project directory
4. Test your changes (both backend and frontend if applicable)
5. Commit your changes: `git commit -m 'Add some feature'`
6. Push to the branch: `git push origin feature/your-feature-name`
7. Submit a pull request

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 3000 and 8080 are available
2. **Java version**: Verify Java 21 is installed and set as default
3. **Node version**: Ensure Node.js 20+ is installed
4. **CORS issues**: Configure CORS in Spring Boot if needed

### Getting Help

- Check individual project READMEs for specific instructions
- Review error logs in terminal outputs
- Ensure all prerequisites are installed

## License

This project is developed as part of a coding test for Bangkok Bank Learning (BBL).

## Contact

For questions or support, please contact the development team.