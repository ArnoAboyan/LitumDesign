FROM openjdk:18 as builder

COPY . /workspace

WORKDIR /workspace

RUN chmod +x ./mvnw

RUN ./mvnw clean package

FROM openjdk:18

WORKDIR /app

COPY --from=builder /workspace/target/LitumDesign-*.jar /app/LitumDesign.jar

ENV google_service_client_privateid=asdasdasd
ENV google_service_client_private=asdasdasd
ENV google_service_clientemail=asdasdasd
ENV google_service_clientid=asdasdasd

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar LitumDesign.jar"]