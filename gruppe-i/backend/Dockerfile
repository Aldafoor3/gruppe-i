# Use the official maven image as the base image
FROM maven:3.8.6-openjdk-18 as build

# Copy the application code to the container
COPY . /app

# Set the working directory in the container
WORKDIR /app

# Build the application
RUN mvn clean package -DskipTests

# Use the official openjdk image as the base image
FROM openjdk:18

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/sep-backend.jar .

# Copy the assets folder to the container
COPY ./src/main/java/com/example/demo/DatasetsDB/assets/ /app/assets/
COPY ./src/main/java/com/example/demo/geoDataDB/assets/ /app/geoDataDB/assets/

# Set environment variables for Postgres configuration
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:3333/postgres
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=admin

# Expose the port on which the application will run
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "sep-backend.jar"]
