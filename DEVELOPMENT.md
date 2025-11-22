# Development Setup Guide

## Prerequisites

- Java 17 or later
  - [Install Java](https://openjdk.java.net/)
  - Verify: `java -version`
  
- Maven 3.6.0 or later
  - [Install Maven](https://maven.apache.org/install.html)
  - Verify: `mvn -version`
  
- MongoDB 5.0 or later
  - [Install MongoDB Community Edition](https://docs.mongodb.com/manual/installation/)
  - [MongoDB Atlas (Cloud)](https://www.mongodb.com/cloud/atlas) for cloud-based MongoDB

- Git
  - [Install Git](https://git-scm.com/downloads)
  - Verify: `git --version`

## Setting Up MongoDB

### Option 1: Local MongoDB

**macOS (using Homebrew):**
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

**Verify MongoDB is running:**
```bash
mongosh  # Connect to MongoDB shell
> exit  # Exit the shell
```

### Option 2: MongoDB Atlas (Cloud)

1. Create an account at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a cluster
3. Get your connection URI
4. Update `application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/projectroadmapdb
   ```

### Option 3: Docker

```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

## Cloning the Project

```bash
git clone https://github.com/Jayanth-stack/projectroadmap.git
cd projectroadmap
```

## Building the Project

```bash
# Clean and package
./mvnw clean package

# Build without running tests (faster)
./mvnw clean package -DskipTests

# Build with verbose output
./mvnw clean package -X
```

## Running the Application

```bash
# Using Maven
./mvnw spring-boot:run

# Using the JAR file
java -jar target/projectroadmap-0.0.1-SNAPSHOT.jar

# With custom properties
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.data.mongodb.uri=mongodb://localhost:27017/customdb"
```

## Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=URLShortenerServiceTest

# Run with coverage report
./mvnw clean test jacoco:report
# Report will be available at: target/site/jacoco/index.html

# Run tests in verbose mode
./mvnw test -X
```

## IDE Setup

### IntelliJ IDEA

1. Open the project folder
2. IntelliJ will automatically detect the Maven project
3. Go to `File > Project Structure > Project`
4. Set Project SDK to Java 17
5. Go to `Run > Edit Configurations`
6. Create a new "Spring Boot" configuration

### VS Code

1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - MongoDB for VS Code

2. Open the project folder
3. Create `.vscode/launch.json`:
   ```json
   {
     "version": "0.2.0",
     "configurations": [
       {
         "type": "java",
         "name": "Spring Boot App",
         "request": "launch",
         "cwd": "${workspaceFolder}",
         "mainClass": "com.example.urlshortner.projectroadmap.ProjectroadmapApplication",
         "projectName": "projectroadmap",
         "args": "",
         "envFile": "${workspaceFolder}/.env"
       }
     ]
   }
   ```

## Project Structure Navigation

```
projectroadmap/
├── src/main/java/com/example/urlshortner/projectroadmap/
│   ├── controller/        # REST API endpoints
│   ├── service/           # Business logic
│   ├── entity/            # MongoDB document models
│   ├── repository/        # Database access
│   ├── dto/               # Data Transfer Objects
│   └── utils/             # Utility classes
├── src/main/resources/
│   └── application.properties  # Configuration
├── src/test/java/        # Unit tests
├── pom.xml                # Maven configuration
└── README.md              # This file
```

## Code Style Guidelines

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and under 20 lines when possible
- Use dependency injection via constructors

## Common Development Tasks

### Adding a New Endpoint

1. Create method in `URLController`
2. Create corresponding method in `URLShortenerService`
3. Add unit tests in `URLShortenerServiceTest`
4. Update API_DOCUMENTATION.md
5. Test with cURL or Postman

### Adding MongoDB Document Field

1. Add field to `urlshortner` entity with getter/setter
2. Update `urlresponse` DTO to expose the field
3. Update mapToResponse() method in service
4. Add tests for the new field
5. Run tests to verify

### Updating Dependencies

```bash
# Check for updates
./mvnw versions:display-dependency-updates

# Update dependencies
./mvnw versions:use-latest-versions

# Update plugins
./mvnw versions:display-plugin-updates
```

## Troubleshooting

### MongoDB Connection Error
```
Exception opening socket: Connection refused
```
**Solution**: Ensure MongoDB is running
```bash
# Check MongoDB status (macOS)
brew services list | grep mongodb

# Start MongoDB (macOS)
brew services start mongodb-community
```

### Port 8080 Already in Use
```
Port 8080 already in use
```
**Solution**: Use different port in application.properties
```properties
server.port=8081
```

### Java Version Mismatch
```
java.lang.UnsupportedClassVersionError: Unsupported major.minor version
```
**Solution**: Ensure Java 17 is installed and set as JAVA_HOME
```bash
java -version
export JAVA_HOME=/path/to/java-17
```

### Tests Fail on MongoDB Connection
Normal if MongoDB isn't running. Tests will still execute with mocked dependencies.
Real MongoDB is only needed for integration tests if you have them.

## Useful Commands

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Clean build directory
./mvnw clean

# Skip tests during build
./mvnw clean package -DskipTests

# Run specific test
./mvnw test -Dtest=URLShortenerServiceTest#testCreateShortenedUrlWithProvidedShortcode

# Check for security vulnerabilities
./mvnw clean package dependency-check:aggregate

# Generate project report
./mvnw site

# Format code
./mvnw spotless:apply
```

## Next Steps

1. Read [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for API details
2. Explore the codebase structure
3. Run the application and test with provided examples
4. Contribute features following the guidelines above

## Getting Help

- Check [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- Read [MongoDB Spring Data Documentation](https://spring.io/projects/spring-data-mongodb)
- Review [JavaDoc in the code](src/main/java)
- Check existing GitHub Issues
