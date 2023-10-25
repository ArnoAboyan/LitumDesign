FROM openjdk:18 as builder

COPY . /workspace

WORKDIR /workspace

RUN chmod +x ./mvnw

RUN ./mvnw clean package

FROM openjdk:18

WORKDIR /app

COPY --from=builder /workspace/target/LitumDesign-*.jar /app/LitumDesign.jar

ARG asdasdasd
ARG asdasdasd1
ARG asdasdasd2
ARG asdasdasd3


ENV google_service_client_privateid=$asdasdasd
ENV google_service_client_private=$asdasdasd1
ENV google_service_clientemail=$asdasdasd2
ENV google_service_clientid=$asdasdasd3

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar LitumDesign.jar"]