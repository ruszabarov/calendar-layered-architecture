# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-oracle

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/data-access-layer-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
