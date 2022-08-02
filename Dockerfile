FROM eclipse-temurin:17.0.3_7-jre-alpine
EXPOSE 8080
ARG JAR_FILE=target/recipes-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]