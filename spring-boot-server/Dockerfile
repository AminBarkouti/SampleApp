# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Add metadata (optional)
LABEL maintainer="Mohamed Amin Barkouti"

# Copy the .jar file to the container
COPY target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar app.jar

# Run the .jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
