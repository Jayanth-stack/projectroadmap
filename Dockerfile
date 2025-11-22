# Build stage
FROM maven:3.9-openjdk-17 AS builder

WORKDIR /app

# Copy pom.xml and source
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-slim

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/projectroadmap-0.0.1-SNAPSHOT.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/urls || exit 1

# Expose port
EXPOSE 8080

# Set environment
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# Run application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
