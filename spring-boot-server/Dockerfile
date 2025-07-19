# Use smaller base image
FROM openjdk:17-alpine

# Add metadata (optional)
LABEL maintainer="Mohamed Amin Barkouti"

# Copy the JAR file
COPY target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
