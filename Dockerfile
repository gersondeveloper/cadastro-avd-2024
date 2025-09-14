# Dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/cadastro-avd-2024-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]