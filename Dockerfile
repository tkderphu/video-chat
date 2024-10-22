FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /build/target/video_chat-*.jar /app/
EXPOSE 8088

ENV DB_URL=jdbc:mysql://app-mysql:3306/social_network_v2
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
#-Dspring.profiles.active=${ACTIVE_PROFILE}
CMD java -jar  -Dspring.datasource.url=${DB_URL}  social-network-${JAR_VERSION}.jar
