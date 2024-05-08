FROM openjdk:17-alpine

RUN mkdir -p /usr/src/app
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} /usr/src/app/backend.jar
WORKDIR /usr/src/app

RUN addgroup -S app && adduser -S app -G app
USER app

ENTRYPOINT ["java", "-jar", "backend.jar"]
