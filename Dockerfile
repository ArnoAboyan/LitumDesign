FROM eclipse-temurin:19 as builder

COPY . /workspace

WORKDIR /workspace

RUN ./mvnw clean package

FROM eclipse-temurin:19

WORKDIR /app

COPY --from=builder /workspace/target/java-springboot-*.jar /app/java-springboot.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar java-springboot.jar"]