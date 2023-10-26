FROM openjdk:18 as builder

COPY . /workspace

WORKDIR /workspace

RUN chmod +x ./mvnw

RUN ./mvnw clean package

FROM openjdk:18

WORKDIR /app

COPY --from=builder /workspace/target/LitumDesign-*.jar /app/LitumDesign.jar

ARG CLIENT_PRIVATEID
ARG CLIENT_PRIVATE
ARG CLIENTMAIL
ARG CLIENTID

ENV google_service_client_privateid=$CLIENT_PRIVATEID
ENV google_service_client_private=$CLIENT_PRIVATE
ENV google_service_clientemail=$CLIENTMAIL
ENV google_service_clientid=$CLIENTID



ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar LitumDesign.jar"]