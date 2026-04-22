FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /build

COPY pom.xml pom.xml
COPY common/pom.xml common/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY payment-service/pom.xml payment-service/pom.xml
COPY common/src common/src
COPY payment-service/src payment-service/src

RUN mvn -pl payment-service -am clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/payment-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
