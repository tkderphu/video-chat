# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/video_chat-*.jar /app/

EXPOSE 8080

ENV DB_URL=jdbc:mysql://app-mysql:3306/video_chat?createDatabaseIfNotExist=true
ENV DB_USERNAME=root
ENV DB_PASSWORD=root

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.datasource.url=${DB_URL} -Dspring.datasource.username=${DB_USERNAME} -Dspring.datasource.password=${DB_PASSWORD}  video_chat-${JAR_VERSION}.jar
