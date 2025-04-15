FROM openjdk:17-jdk-alpine

RUN mkdir -p /var/log/camperparkstaryfolwark && \
    chmod -R 755 /var/log/camperparkstaryfolwark

VOLUME /tmp
VOLUME ["/var/log/camperparkstaryfolwark"]

ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS

COPY target/demo-0.0.1-SNAPSHOT.jar camperparkstaryfolwark.jar

EXPOSE 8080

CMD ["java", "-jar", "camperparkstaryfolwark.jar"]