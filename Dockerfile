# Use OpenJDK 17
FROM eclipse-temurin:17-jdk
# Set working directory inside container
WORKDIR /app

# Copy the jar built by Maven
COPY target/*.jar app.jar

# Expose port 81 inside container
EXPOSE 81

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
