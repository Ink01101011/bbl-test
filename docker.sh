#!/bin/bash

# BBL Project Docker Build and Run Script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Print colored output
print_message() {
    echo -e "${2}${1}${NC}"
}

# Show usage
show_usage() {
    print_message "Usage: $0 [COMMAND]" $BLUE
    echo
    print_message "Commands:" $YELLOW
    echo "  build     - Build all Docker images"
    echo "  up        - Start all services in production mode"
    echo "  dev       - Start all services in development mode"
    echo "  down      - Stop all services"
    echo "  logs      - Show logs from all services"
    echo "  clean     - Remove all containers and images"
    echo "  backend   - Build and run only backend"
    echo "  frontend  - Build and run only frontend"
    echo "  help      - Show this help message"
}

# Build all images
build_all() {
    print_message "Building all Docker images..." $BLUE
    docker-compose build --no-cache
    print_message "Build completed successfully!" $GREEN
}

# Start production services
start_production() {
    print_message "Starting services in production mode..." $BLUE
    docker-compose up -d
    print_message "Services started successfully!" $GREEN
    print_message "Frontend: http://localhost:3000" $YELLOW
    print_message "Backend: http://localhost:8080" $YELLOW
}

# Start development services
start_development() {
    print_message "Starting services in development mode..." $BLUE
    docker-compose -f docker-compose.dev.yml up -d
    print_message "Development services started successfully!" $GREEN
    print_message "Frontend: http://localhost:3000" $YELLOW
    print_message "Backend: http://localhost:8080" $YELLOW
    print_message "Backend Debug: localhost:5005" $YELLOW
}

# Stop all services
stop_services() {
    print_message "Stopping all services..." $BLUE
    docker-compose down
    docker-compose -f docker-compose.dev.yml down
    print_message "Services stopped successfully!" $GREEN
}

# Show logs
show_logs() {
    print_message "Showing logs from all services..." $BLUE
    docker-compose logs -f
}

# Clean up
clean_up() {
    print_message "Cleaning up containers and images..." $YELLOW
    docker-compose down --rmi all --volumes --remove-orphans
    docker-compose -f docker-compose.dev.yml down --rmi all --volumes --remove-orphans
    print_message "Cleanup completed!" $GREEN
}

# Build and run backend only
backend_only() {
    print_message "Building and running backend only..." $BLUE
    docker-compose up --build -d bbl-backend
    print_message "Backend started successfully!" $GREEN
    print_message "Backend: http://localhost:8080" $YELLOW
}

# Build and run frontend only
frontend_only() {
    print_message "Building and running frontend only..." $BLUE
    docker-compose up --build -d bbl-web-app
    print_message "Frontend started successfully!" $GREEN
    print_message "Frontend: http://localhost:3000" $YELLOW
}

# Main script logic
case "${1:-help}" in
    build)
        build_all
        ;;
    up)
        start_production
        ;;
    dev)
        start_development
        ;;
    down)
        stop_services
        ;;
    logs)
        show_logs
        ;;
    clean)
        clean_up
        ;;
    backend)
        backend_only
        ;;
    frontend)
        frontend_only
        ;;
    help|*)
        show_usage
        ;;
esac