FROM openjdk:21-slim

WORKDIR /app
COPY target/r10y-characters-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "app.jar"]