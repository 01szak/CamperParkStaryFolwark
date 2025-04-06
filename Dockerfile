FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/demo-0.0.1-SNAPSHOT.jar camperparkstaryfolwark.jar
EXPOSE 8080
CMD ["java", "-jar", "camperparkstaryfolwark.jar"]
