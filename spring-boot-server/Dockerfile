# Use multi-stage build to reduce final image size
# Stage 1: Build the application
FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Stage 2: Create lightweight runtime image
FROM openjdk:17-jre-slim

# Add metadata
LABEL maintainer="Mohamed Amin Barkouti"

# Copy only the built jar from the builder stage
COPY --from=builder /app/target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar app.jar

# Optimize JVM for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run as non-root user for security
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

ENTRYPOINT ["java", "-jar", "/app.jar"]