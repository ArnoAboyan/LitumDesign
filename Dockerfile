FROM openjdk:17 as builder

COPY . /workspace

WORKDIR /workspace

RUN chmod +x ./mvnw

RUN ./mvnw clean package

FROM openjdk:17

WORKDIR /app

COPY --from=builder /target/java-springboot-*.jar /app/java-springboot.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar java-springboot.jar"]