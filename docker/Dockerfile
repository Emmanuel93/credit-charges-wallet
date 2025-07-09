# ===== 1. Build stage: compila el jar con Maven =====
FROM maven:3.9.7-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# ===== 2. Runtime stage: solo el jar, usuario seguro =====
FROM eclipse-temurin:17-jre-alpine

RUN addgroup --system spring && adduser --system spring --ingroup spring

WORKDIR /app

COPY --from=build /app/target/credit-charges-wallet-1.0-SNAPSHOT.jar app.jar

USER spring©

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
