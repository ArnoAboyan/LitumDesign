FROM openjdk:18 as builder

COPY ./target

WORKDIR /target

RUN ./mvnw clean package

FROM openjdk:18

WORKDIR /app

COPY --from=builder /target/java-springboot.jar /app/java-springboot.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar java-springboot.jar"]