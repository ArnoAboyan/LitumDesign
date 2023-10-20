FROM openjdk:18 as builder

COPY . /workspace

WORKDIR /workspace

RUN chmod +x ./mvnw

RUN ./mvnw clean package

FROM openjdk:18

WORKDIR /app

COPY --from=builder /workspace/target/java-springboot-0.0.1-SNAPSHOT.jar /app/java-springboot-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar java-springboot-0.0.1-SNAPSHOT.jar"]