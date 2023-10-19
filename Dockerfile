FROM openjdk:18.0.2.1
EXPOSE 8080
COPY litumdesign-docker.jar lilitumdesign-docker.jar
ENTRYPOINT ["java","-jar","/litumdesign-docker.jar"]