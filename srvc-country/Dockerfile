FROM openjdk:22

WORKDIR /app
COPY target/country-service-0.0.1-SNAPSHOT.jar /app/srvc-country.jar
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "srvc-country.jar"]