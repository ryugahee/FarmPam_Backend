# Use the official Gradle image as a base image
FROM gradle:latest as builder

# Set the working directory in the container
WORKDIR /app

# Copy the build script and settings file
COPY build.gradle settings.gradle /app/

# Copy the entire project
COPY src /app/src

# Build the application
RUN gradle build --no-daemon

# Create a new image with the JRE
FROM adoptopenjdk/openjdk11:alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080
# Command to run the application
CMD ["java", "-jar", "app.jar"]
