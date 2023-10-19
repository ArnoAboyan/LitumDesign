FROM openjdk:18
EXPOSE 8080
ADD target/litumdesign-docker.jar litumdesign-docker.jar
ENTRYPOINT ["java","-jar","/litumdesign-docker.jar"]