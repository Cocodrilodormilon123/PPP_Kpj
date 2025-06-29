FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/practica-ms-0.0.1-SNAPSHOT.jar app.jar

# Variables de entorno para conectar a Config Server y Eureka
ENV CONFIG_SERVER_HOST=null
ENV EUREKA_URL=null
ENTRYPOINT ["java","-jar","/app.jar"]