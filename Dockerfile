#FROM openjdk:18
#VOLUME /tmp
#EXPOSE 8080
#ARG JAR_FILE=target/litumdesign-docker.jar
#ADD ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#FROM openjdk:18
#ADD target/LitumDesign-0.0.1-SNAPSHOT.jar LitumDesign-0.0.1-SNAPSHOT.jar
#EXPOSE 3000
#ENTRYPOINT ["java", "-jar", "LitumDesign-0.0.1-SNAPSHOT.jar"]

FROM openjdk:18
COPY target/LitumDesign-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","application.jar"]

#FROM openjdk:18
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app.jar"]