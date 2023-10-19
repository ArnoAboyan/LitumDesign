FROM openjdk:18.0.2.1
EXPOSE 8080
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
ADD target/litumdesign-docker.jar lilitumdesign-docker.jar
ENTRYPOINT ["java","-jar","/litumdesign-docker.jar"]