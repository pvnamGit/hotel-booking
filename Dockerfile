# Use OpenJDK 11 as the base image for the builder
FROM openjdk:11-slim AS builder

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean install

# Use OpenJDK 11 as the base image for the runtime
FROM openjdk:11-slim

# Set the working directory
WORKDIR /app

# Copy the packaged jar from the builder stage
COPY --from=builder /app/target/*.jar hotel-booking.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev", "hotel-booking.jar"]