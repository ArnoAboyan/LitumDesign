#FROM openjdk:18
#EXPOSE 8080
#COPY target/litumdesign-docker.jar /usr/app/
#WORKDIR /usr/app
#ENTRYPOINT ["java","-jar","litumdesign-docker.jar"]

FROM openjdk:18
ARG JAR_FILE=target/litumdesign-docker.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]