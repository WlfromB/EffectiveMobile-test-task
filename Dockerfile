FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/EffectiveMobile-test-task-3.3.2.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]