FROM openjdk:18.0.2.1
ARG JAR_FILE=target/*.jar
COPY .target/litumdesign-docker.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]