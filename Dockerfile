FROM openjdk:18 as builder

COPY . /workspace

WORKDIR /workspace

RUN chmod +x ./mvnw

RUN ./mvnw clean package

FROM openjdk:18

WORKDIR /app

COPY --from=builder /workspace/target/LitumDesign-*.jar /app/LitumDesign.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar LitumDesign.jar"]