# URL Shortener - Spring Boot Project

A RESTful web service for creating and managing shortened URLs, built with Spring Boot, MongoDB, and Java 17.

## Features

- **Create Shortened URLs**: Convert long URLs into short, shareable codes
- **Retrieve URLs**: Access original URLs using shortcodes
- **Update URLs**: Modify the target URL for an existing shortcode
- **Delete URLs**: Remove shortened URLs from the system
- **Access Tracking**: Automatically tracks access count and last accessed timestamp

## Project Structure

```
src/
├── main/
│   ├── java/com/example/urlshortner/projectroadmap/
│   │   ├── ProjectroadmapApplication.java       (Spring Boot entry point)
│   │   ├── controller/
│   │   │   └── URLController.java               (REST API endpoints)
│   │   ├── service/
│   │   │   └── urlshortnerservice.java          (Business logic)
│   │   ├── entity/
│   │   │   └── urlshortner.java                 (MongoDB document model)
│   │   ├── repository/
│   │   │   └── urlrepository.java               (MongoDB repository)
│   │   ├── dto/
│   │   │   ├── createUrl.java                   (Create request DTO)
│   │   │   ├── updateurlrequest.java            (Update request DTO)
│   │   │   └── urlresponse.java                 (Response DTO)
│   │   └── utils/
│   │       └── shortcodegenerator.java          (Utility for generating shortcodes)
│   └── resources/
│       └── application.properties               (Configuration)
└── test/
    └── java/com/example/urlshortner/projectroadmap/
        └── URLShortenerServiceTest.java         (Unit tests)
```

## Prerequisites

- Java 17 or later
- Maven 3.6.0 or later
- MongoDB 5.0 or later (running on localhost:27017)

## Configuration

Edit `src/main/resources/application.properties` to configure MongoDB connection:

```properties
spring.application.name=projectroadmap
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/projectroadmapdb
```

## Building the Project

```bash
# Build the project
./mvnw clean package

# Build without running tests
./mvnw clean package -DskipTests
```

## Running the Application

```bash
# Run the application
./mvnw spring-boot:run

# Or run the JAR file
java -jar target/projectroadmap-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Running Tests

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=URLShortenerServiceTest
```

## API Endpoints

### 1. Create a Shortened URL

**POST** `/api/urls`

Request Body:
```json
{
  "originalURL": "https://www.example.com/very-long-url",
  "shortcode": "abc123"  // optional - auto-generated if not provided
}
```

Response (201 Created):
```json
{
  "shortcode": "abc123",
  "originalURL": "https://www.example.com/very-long-url",
  "createdAt": "2025-11-22T17:58:00Z",
  "lastAccessedAt": null,
  "accessCount": 0
}
```

### 2. Retrieve a URL

**GET** `/api/urls/{shortcode}`

Response (200 OK):
```json
{
  "shortcode": "abc123",
  "originalURL": "https://www.example.com/very-long-url",
  "createdAt": "2025-11-22T17:58:00Z",
  "lastAccessedAt": "2025-11-22T17:59:00Z",
  "accessCount": 1
}
```

**Note**: Each retrieval increments the access count and updates the last accessed timestamp.

### 3. Update a URL

**PUT** `/api/urls/{shortcode}`

Request Body:
```json
{
  "originalURL": "https://www.example.com/new-url"
}
```

Response (200 OK):
```json
{
  "shortcode": "abc123",
  "originalURL": "https://www.example.com/new-url",
  "createdAt": "2025-11-22T17:58:00Z",
  "lastAccessedAt": "2025-11-22T17:59:00Z",
  "accessCount": 1
}
```

### 4. Delete a URL

**DELETE** `/api/urls/{shortcode}`

Response (204 No Content)

## Example Usage with cURL

```bash
# Create a shortened URL
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"originalURL": "https://www.example.com/long-url"}'

# Retrieve a URL
curl http://localhost:8080/api/urls/abc123

# Update a URL
curl -X PUT http://localhost:8080/api/urls/abc123 \
  -H "Content-Type: application/json" \
  -d '{"originalURL": "https://www.example.com/new-url"}'

# Delete a URL
curl -X DELETE http://localhost:8080/api/urls/abc123
```

## Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: MongoDB 5.0+
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **Logging**: SLF4J + Logback

## Key Components

### URLShortenerService
Core service class that handles:
- Creating shortened URLs with auto-generated or custom shortcodes
- Retrieving URLs and tracking access statistics
- Updating existing URLs
- Deleting URLs

### URLController
REST API controller with four main endpoints:
- `POST /api/urls` - Create shortened URL
- `GET /api/urls/{shortcode}` - Retrieve URL
- `PUT /api/urls/{shortcode}` - Update URL
- `DELETE /api/urls/{shortcode}` - Delete URL

### ShortCodeGenerator
Utility class for generating random 6-character shortcodes using alphanumeric characters.

## Unit Tests

The project includes comprehensive unit tests covering:
- Creating URLs with provided and auto-generated shortcodes
- Handling duplicate shortcode scenarios
- Retrieving and updating URLs
- Deleting URLs
- Error cases

All tests use Mockito for mocking dependencies and achieve high code coverage.

## Future Enhancements

- Add custom shortcode validation
- Implement rate limiting
- Add expiration dates for shortened URLs
- Create analytics dashboard
- Add authentication and authorization
- Implement caching for frequently accessed URLs
- Add batch operations for bulk URL creation
- Create a web UI for the service

## License

This project is part of the Project Roadmap initiative.

## Author

Created as a Spring Boot demonstration project for URL shortening functionality.
