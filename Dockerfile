# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Gradle wrapper + config first for better caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Pre-download dependencies (caches layers)
RUN ./gradlew dependencies --no-daemon || true

# Copy rest of the source
COPY src src

# Build the JAR
RUN ./gradlew bootJar -x test --no-daemon


# ---- Runtime stage ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
