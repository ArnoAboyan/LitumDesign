FROM openjdk:18.0.2.1
EXPOSE 8080
COPY target/litumdesign-docker.jar litumdesign-docker.jar
ENTRYPOINT ["java","-jar","/litumdesign-docker.jar"]