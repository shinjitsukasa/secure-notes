# Use an official OpenJDK runtime as a parent image
FROM bellsoft/liberica-runtime-container:jdk-21-stream-musl AS builder

# Set the working directory in the container
WORKDIR /app
ADD notes /app/notes

# Ensure the mvnw script is executable
RUN chmod +x /app/notes/mvnw

# Build the application
RUN cd notes && mvnw clean package

FROM bellsoft/liberica-runtime-container:jre-21-slim-musl

WORKDIR /app
# Expose the port the app runs on
EXPOSE 8090

# Copy the jar file from the builder stage
COPY --from=builder /app/notes/target/notes-0.0.1-SNAPSHOT.jar /app/notes-0.0.1-SNAPSHOT.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "-XX:MaxRAMPercentage=75", "-XX:MinRAMPercentage=25", "/app/notes-0.0.1-SNAPSHOT.jar"]

# docker build -t notes-be .