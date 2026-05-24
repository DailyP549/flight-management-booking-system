FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY pom.xml .

# Download dependencies
RUN apk add --no-cache maven && \
    mvn dependency:go-offline -B

# Copy all Java source files
COPY *.java ./

# Copy frontend files
COPY index.html .
COPY styles.css .
COPY app.js .
COPY three-background.js .

# Build the application (clearing cache)
RUN mvn clean package -DskipTests -B

# Expose port
EXPOSE 10000

# Start the application
CMD ["java", "-jar", "target/flight-management-booking-system-1.0.0.jar"]
