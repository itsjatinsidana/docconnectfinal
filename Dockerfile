# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/docCONNECT.jar /app/docCONNECT.jar

# Expose port 8080 to the outside world
EXPOSE 5000

# Run the JAR file
ENTRYPOINT ["java", "-jar", "docCONNECT.jar"]

