# =========================
# Stage 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build

# Copy Maven wrapper + project files first (better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

# Copy sources and build
COPY src src
RUN ./mvnw -B clean package


# =========================
# Stage 2: Runtime
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /build/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
