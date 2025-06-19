FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
#copy the route of .jar build , app.jar = name of image in Docker is a Alias
COPY target/registry-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]