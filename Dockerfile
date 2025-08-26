# Use official OpenJDK 17 runtime
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (to leverage caching)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (offline mode for caching)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "target/travel-planner-0.0.1-SNAPSHOT.jar"]
