FROM gradle:9-jdk25 AS build

WORKDIR /app

COPY . .

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
#COPY build/libs/AuthenticationService-1.0-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]