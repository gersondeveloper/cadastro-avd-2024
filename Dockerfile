# Use official OpenJDK 17 as base image
FROM eclipse-temurin:17-jre

# Set the working directory
WORKDIR /app

# Copy the JAR file (assuming it's built and located in target/)
COPY target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
