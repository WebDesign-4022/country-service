FROM openjdk:22
LABEL authors="dshmmd"

WORKDIR /app
COPY target/country-service-0.0.1-SNAPSHOT.jar /app/srvc-countries.jar
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "srvc-countries.jar"]