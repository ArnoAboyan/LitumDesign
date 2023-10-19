FROM openjdk:18
EXPOSE 8080
COPY target/litumdesign-docker.jar /usr/app/
WORKDIR /usr/app
ENTRYPOINT ["java","-jar","litumdesign-docker.jar"]