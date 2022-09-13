FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} raven-api.jar

ENTRYPOINT ["java","-jar","/raven-api.jar"]