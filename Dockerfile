# Stage 1: Build the application using Maven
FROM maven:3.8.8-eclipse-temurin-11 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
COPY src ./src

# Run Maven to build the application
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar hotel-booking.jar

# Expose the application's port
EXPOSE 8080

# Set the default command to run the application
ENTRYPOINT ["java", "-jar", "hotel-booking.jar"]

# Set the active Spring profile (adjust as needed)
ENV SPRING_PROFILES_ACTIVE=dev