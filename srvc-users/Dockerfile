FROM openjdk:22
LABEL authors="dshmmd"

WORKDIR /app
COPY target/country-service-0.0.1-SNAPSHOT.jar /app/srvc-users.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "srvc-users.jar"]