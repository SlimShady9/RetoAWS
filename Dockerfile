FROM maven:3.9.11-eclipse-temurin-21 AS build
          
WORKDIR /workspace

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/target/awschallenge-0.0.1-SNAPSHOT.jar app.jar
COPY certs/global-bundle.pem /app/global-bundle.pem
COPY certs/global-bundle.pem /app/.postgresql/root.crt
RUN groupadd --system spring && useradd --system --gid spring --home-dir /app --no-create-home spring \
	&& chown -R spring:spring /app

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]