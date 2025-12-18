# Dockerfile (multi-stage build)

# 1) Build stage: build the Spring Boot JAR using official Maven image
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

# Copy pom and download dependencies first for better caching
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -DskipTests package

# 2) Runtime stage: minimal JRE image to run the app
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy fat jar from the builder stage
COPY --from=builder /build/target/cadastro-avd-2024-*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]