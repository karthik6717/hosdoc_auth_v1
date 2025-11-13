# syntax=docker/dockerfile:1

### Build stage ###
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copy mvnw and wrapper first to leverage build cache
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Copy sources and build
COPY src ./src
RUN chmod +x ./mvnw && ./mvnw -B -DskipTests clean package

### Run stage ###
FROM eclipse-temurin:17-jre
WORKDIR /app

# Use ARG to pick up the jar even if the version changes
ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/${JAR_FILE} ./app.jar

# If your app listens on 8081 by default, expose it; you can override with SERVER_PORT
EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]
