# Use an official OpenJDK runtime
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies (caching)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Run the Spring Boot app
CMD ["java", "-jar", "target/travel-planner-0.0.1-SNAPSHOT.jar"]
