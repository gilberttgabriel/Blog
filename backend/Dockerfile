FROM  maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Traemos SOLO el archivo .jar compilado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Spring Boot usa el puerto 8080 por defecto
EXPOSE 8080

# El comando que enciende el motor de Spring Boot
CMD ["java", "-jar", "app.jar"]