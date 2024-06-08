# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application's jar to the container
COPY target/your-app.jar /app/hotel-booking.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "your-app.jar"]
ENV SPRING_PROFILES_ACTIVE dev
