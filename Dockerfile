# ──── Etapa 1: build ────
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Descargar dependencias primero (aprovecha el cache de Docker)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Compilar el proyecto
COPY src ./src
RUN mvn -B clean package -DskipTests

# ──── Etapa 2: runtime ────
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
